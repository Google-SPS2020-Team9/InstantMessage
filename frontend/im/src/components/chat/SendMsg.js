import React from "react";
import { message, Button, Input } from "antd";
import { Context } from "../../context/ContextSource";
import "./SendMsg.css";

class SendMsg extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      submitting: false,
      content: "",
    };
  }

  handleContentChange = (e) => {
    this.setState({ content: e.target.value });
  };

  static contextType = Context;

  handleSubmit = () => {
    if (this.state.content === "") {
      message.error("Cannot send an empty message");
      return;
    }
    this.setState({ submitting: true });
    this.context.conn.send(
      JSON.stringify({
        request_id: Math.random().toString(),
        type: "send message",
        content: this.state.content,
      })
    );
  };

  onEnterPress = (e) => {
    if (e.keyCode === 13 && e.shiftKey === false) {
      e.preventDefault();
      this.handleSubmit();
    }
  };

  UNSAFE_componentWillReceiveProps(props) {
    // TODO: not an elegant way to pass two vars. Need to rewrite.
    this.setState({ submitting: props.submitting });
    if (props.content === "") this.setState({ content: "" });
  }

  render() {
    return (
      <div id="comment-row-container">
        <Input.TextArea
          rows={4}
          value={this.state.content}
          onChange={this.handleContentChange}
          onKeyDown={this.onEnterPress}
        />
        <Button
          id="send-msg-btn"
          loading={this.state.submitting}
          htmlType="submit"
          type="primary"
        >
          Send
        </Button>
      </div>
    );
  }
}

export default SendMsg;
