import React from "react";
import { message, Button, Form, Input, Modal } from "antd";
import { Context } from "../../context/ContextSource";

class SignInModal extends React.Component {
  static contextType = Context;

  constructor(props) {
    super(props);
    this.wrapper = React.createRef();
    this.state = {
      userName: null,
    };
  }

  /**
   * Handling user login.
   */
  handleLogin = () => {
    console.log("SignInModal::handleLogin");

    this.context.conn.send(JSON.stringify(
      {
        request_id: Math.random().toString(),
        type: "sign in",
        username: this.state.userName
      }
    ));

    this.context.setUserName(this.state.userName);
    this.context.closeSignInModal();
  };

  /**
   * Handling input field changes.
   */
  handleUserName = (e) => {
    this.setState({ userName: e.target.value });
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
        title="Enter your username"
        centered
        className="sign-in-modal"
        visible={this.context.signInModalVisibility}
        footer={null}
        onCancel={() => {
          this.context.user === null
            ? message.warning(
                "You need to have a username to use Instant Message!",
                3
              )
            : this.context.closeSignInModal();
        }}
      >
        <p>
          You've successfully entered room {this.context.roomName}. The next
          step is to have a username. Note that it cannot be the same with other
          room members. If leaving blank, you will be given a random user name.{" "}
        </p>
        <Form onFinish={this.handleLogin} {...layout}>
          <Form.Item label="UserName" name="username">
            <Input
              value={this.state.roomNumber}
              onChange={this.handleRoomNumber}
            />
          </Form.Item>

          <Form.Item {...tailLayout}>
            <Button type="primary" htmlType="submit">
              Start Chat
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    );
  }
}

export default SignInModal;
