import React from "react";
import { message, Button, Form, Input, Modal, Tabs } from "antd";
import { Context } from "../../context/ContextSource";
import { Connection } from "../../context/Connection"

import config from "../../config";
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

    const connection = new Connection(this.state.roomid);
    this.context.setConn(connection);
    connection.addHandler("room state", (data) => {
      if (data.success === true) {
        console.log(
          "[room state]: joining successful. Room id: " + data.room.id
        );
        this.context.setRoomId(data.room.id);
        this.context.setRoomName(data.room.name);
        this.context.closeSelectRmModal();
        this.context.showSignInModal();
      } else {
        console.log("[room state]: joining unsuccessful.");
        this.context.setConn(null);
        if (data.error === "Room not exist") {
          message.error("Room doesn't exist.");
        } else {
          message.error("An error occurred. Please try again.");
          console.error(data);
        }
      }
    })
  }

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
    fetch(`http://${config.host}/room`, requestOptions)
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
