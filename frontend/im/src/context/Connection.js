import config from "../config";

export class Connection {

  constructor(roomId) {
    this.handlers = {};
    this.requestPool = {}
    this.conn = new WebSocket(`ws://${config.host}/room/${roomId}`);
    this.conn.onmessage = (event) => {
      const data = JSON.parse(event.data);
      const handler = this.handlers[data.type];
      if (!handler) {
        throw new Error(`cannot find handler for type ${data.type}`);
      }
      handler(data);
    };
  }

  addHandler(type, handler) {
    if (this.handlers[type]) {
      throw new Error(`handler for type ${type} already exist`);
    }
    this.handlers[type] = handler;
  }

  request(type, data) {
    this.registerRequest(type);
    const requestId = Math.random().toString()
    data = {
      type,
      request_id: requestId,
      ...data
    }
    this.conn.send(JSON.stringify(data));

    return new Promise((resolve, reject) => {
      const timeoutId = setTimeout(() => {
        delete this.requestPool[type][requestId]
        reject({
          success: false,
          error: 'timeout'
        })
      }, 5000)
      this.requestPool[type][requestId] = (data) => {
        clearTimeout(timeoutId)
        if (data.success) {
          resolve(data)
        } else {
          reject(data)
        }
      }

    })
  }

  registerRequest(type) {
    if (this.requestPool[type] !== undefined) {
      return
    }
    this.requestPool[type] = {}
    this.addHandler(`${type} result`, (data) => {
      const handler = this.requestPool[type][data.request_id]
      if (handler) {
        handler(data)
      }
    })
  }

}
