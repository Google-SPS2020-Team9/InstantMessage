import React from "react";
import { message, Button, Form, Input, Modal, Tabs } from "antd";
import { Context } from "../../context/ContextSource";
import axios from "axios";

import "./SelectRmModal.css";

const { TabPane } = Tabs;

class SelectRmModal extends React.Component {
  static contextType = Context;

  constructor(props) {
    super(props);
    this.state = {
      activeTab: "enter",
      roomid: null,
      roomName: null,
    };
  }

  /**
   * Handling enter room for all. A room creator also needs to enter the room.
   */
  handleEnterRoom = () => {
    console.log("SelectRmModal::handleEnterRoom");
    console.log(this.context.host);
    console.log(this.state.roomid);
    this.context.setConn(
      new WebSocket(`ws://${this.context.host}/room/${this.state.roomid}`)
    );
    this.context.closeSelectRmModal();
    this.context.showSignInModal();
  };

  /**
   * Handling creating a room. Triggers enter room automatically inside.
   */
  handleCreateRoom = () => {
    console.log("SelectRmModal::handleCreateRoom");
    axios
      .post(`http://${this.context.host}/room`, {
        name: this.state.roomName,
      })
      .then((res) => {
        if (res.status === 202) {
          this.setState({ roomid: res.room.id });
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
        onCancel={() => {
          this.context.user === null
            ? message.warning("You have to log in to use Instant Message!", 3)
            : this.context.closeSelectRmModal();
        }}
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
