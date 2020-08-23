import React from "react";
import {message, Button, Input} from "antd";
import {Context} from "../../context/ContextSource";
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
    this.setState({content: e.target.value});
  };

  static contextType = Context;

  handleSubmit = () => {
    if (this.state.content === "") {
      message.error("Cannot send an empty message");
      return;
    }
    this.setState({submitting: true});
    this.context.conn.request("send message", {content: this.state.content}).then(data => {
      this.setState({content: ""});
      console.log(`[message sent] ${data.message.owner.name}: ${data.message.content}`);
    }).catch(data => {
      message.error("Message unsent. Please try again.");
      console.error(data);
    }).finally(() => {
      this.setState({submitting: false});
    })
  };

  onEnterPress = (e) => {
    if (e.keyCode === 13 && e.shiftKey === false) {
      e.preventDefault();
      this.handleSubmit();
    }
  };

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
