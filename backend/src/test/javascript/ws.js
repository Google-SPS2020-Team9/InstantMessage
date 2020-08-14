var ws = new WebSocket('ws://localhost:8080/room/1');

ws.onmessage = function (evt) {
    var json = JSON.parse(evt.data)
    if (json.success === false) {
        console.error(json)
        return
    }
    if (json.type === 'push messages') {
        var messages = json.messages
        for (var message of messages) {
            console.log('[ws receive] ' + message.owner.name + ": " + message.content)
        }
    }
};

ws.onclose = function (evt) {
    console.log('[ws close]');
};

ws.send(JSON.stringify(
    {
        request_id: Math.random().toString(),
        type: "sign in",
        username: "User1"
    }
))

ws.send(JSON.stringify(
    {
        request_id: Math.random().toString(),
        type: "send message",
        content: "some message content"
    }
))