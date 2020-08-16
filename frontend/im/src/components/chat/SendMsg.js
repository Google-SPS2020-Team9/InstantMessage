import React from "react";
import { message, Alert, Button, Input } from "antd";
import { Context } from "../../context/ContextSource";
import "./SendMsg.css";

class SendMsg extends React.Component {
  state = {
    submitting: false,
    content: "",
    showAlert: false,
    alertText: "",
  };

  handleContentChange = (e) => {
    this.setState({ content: e.target.value });
  };

  static contextType = Context;

  handleSubmit = () => {
    this.setState({ submitting: true });

    this.context.conn.send(
      JSON.stringify({
        request_id: Math.random().toString(),
        type: "send message",
        content: this.state.content,
      })
    );

    this.setState({ content: "", submitting: false });
  };

  render() {
    return (
      <div>
        {this.state.showAlert ? (
          <Alert
            message={this.state.alertText}
            type="warning"
            closable={true}
            onClose={this.handleCloseAlert}
          />
        ) : null}
        <div className="comment-row-container">
          <Input.TextArea rows={1} onChange={this.handleContentChange} />
          <Button
            className="send-msg-btn"
            htmlType="submit"
            loading={this.state.submitting}
            onClick={this.handleSubmit}
            type="primary"
          >
            Send
          </Button>
        </div>
      </div>
    );
  }
}

export default SendMsg;
