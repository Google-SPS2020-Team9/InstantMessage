import React from "react";
import { message, Button, Form, Input, Modal, Tabs } from "antd";
import { Context } from "../../context/ContextSource";

import "./AuthModal.css";

const { TabPane } = Tabs;

class SelectRmModal extends React.Component {
  static contextType = Context;

  constructor(props) {
    super(props);
    this.state = {
      activeTab: "enter",
      roomid: "",
      roomName: "",
    };
  }

  /**
   * Handling enter room for all. A room creator also needs to enter the room.
   */
  handleEnterRoom = () => {
    console.log("SelectRmModal::handleEnterRoom");
    if (this.state.roomid === "") return;
    this.context.setConn(
      new WebSocket(`ws://${this.context.host}/room/${this.state.roomid}`)
    );
    this.context.setRoomId(this.state.roomid);
    this.context.setRoomName(this.state.roomName);
    this.context.closeSelectRmModal();
    this.context.showSignInModal();
  };

  /**
   * Handling creating a room. Triggers enter room automatically inside.
   */
  handleCreateRoom = () => {
    if (this.state.roomName === "") return;
    console.log("SelectRmModal::handleCreateRoom");
    const requestOptions = {
      method: "POST",
      body: JSON.stringify({ name: this.state.roomName }),
    };
    fetch(`http://${this.context.host}/room`, requestOptions)
      .then((res) => res.json())
      .then((jsondata) => {
        console.log(jsondata);
        if (jsondata.success === true) {
          this.setState({
            roomid: jsondata.room.id,
            roomName: jsondata.room.name,
          });
          this.handleEnterRoom();
        }
      })
      .catch((err) => {
        console.log(err);
        message.error("An error occurred.");
      });
  };

  /**
   * Handling input field changes.
   */
  handleRoomName = (e) => {
    this.setState({ roomName: e.target.value });
  };

  handleRoomId = (e) => {
    this.setState({ roomid: e.target.value });
  };

  render() {
    const layout = {
      labelCol: { span: 0 },
      wrapperCol: { span: 16 },
    };
    const tailLayout = {
      wrapperCol: { offset: 0, span: 16 },
    };

    return (
      <Modal
        title="Welcome to Instant Message!"
        centered
        className="select-room-modal"
        visible={this.context.selectRmModalVisibility}
        footer={null}
      >
        <Tabs
          defaultActiveKey="enter"
          onChange={(t) => this.setState({ activeTab: t })}
          activeKey={this.state.activeTab}
          size="default"
        >
          <TabPane tab="Enter a room" key="enter">
            <p>
              Please enter an existing room number to enter a room, or create a
              new room by providing a name.
            </p>
            <Form onFinish={this.handleEnterRoom} {...layout}>
              <Form.Item
                label="Room Number"
                name="roomid"
                rules={[
                  { required: true, message: "Please enter a room number." },
                  { whitespace: true, message: "Room number cannot be empty." },
                ]}
              >
                <Input value={this.state.roomid} onChange={this.handleRoomId} />
              </Form.Item>

              <Form.Item {...tailLayout}>
                <Button type="primary" htmlType="submit">
                  Enter
                </Button>
              </Form.Item>
            </Form>
          </TabPane>
          <TabPane tab="Create a room" key="create">
            <p>
              Please enter an existing room number to enter a room, or create a
              new room by providing a name.
            </p>
            <Form onFinish={this.handleCreateRoom} {...layout}>
              <Form.Item
                label="Room Name"
                name="roomname"
                rules={[
                  {
                    required: true,
                    message: "Please enter a valid room name.",
                  },
                  { whitespace: true, message: "Room name cannot be empty." },
                ]}
              >
                <Input
                  value={this.state.roomName}
                  onChange={this.handleRoomName}
                />
              </Form.Item>

              <Form.Item {...tailLayout}>
                <Button type="primary" htmlType="submit">
                  Create
                </Button>
              </Form.Item>
            </Form>
          </TabPane>
        </Tabs>
      </Modal>
    );
  }
}

export default SelectRmModal;
