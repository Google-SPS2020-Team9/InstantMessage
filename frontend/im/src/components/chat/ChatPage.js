import React from "react";
import { Button, Card, Divider, List, PageHeader, message } from "antd";
import MsgEntry from "./MsgEntry";
import SendMsg from "./SendMsg";
import { Context } from "../../context/ContextSource";
import TestMsgs from "../../testData/msgs";
import "./ChatPage.css";

class ChatPage extends React.Component {
  state = {
    msgs: [],
    submitting: false,
  };

  static contextType = Context;

  componentDidMount() {
    this.setState({
      msgs: TestMsgs.messages,
    });
    this.context.conn.onmessage = (e) => {
      const data = JSON.parse(e.data);
      if (data.success === false) {
        console.error(data);
        return;
      }
      if (data.type === "push messages") {
        for (var msg of data.messages) {
          this.setState((prevState) => ({
            msgs: [...prevState.msgs, msg],
          }));
          console.log(this.state.msgs);
          console.log(
            "[message received] " + msg.owner.name + ": " + msg.content
          );
        }
      } else if (data.type === "sign in result") {
        this.context.setUserId(data.user.id);
        this.context.setUserName(data.user.name);
        message.success("Login successful as " + this.context.username);
        console.log("[signing in] " + data.user.id + ": " + data.user.name);
      } else if (data.type === "send message result") {
        this.setState({ submitting: false });
        console.log(
          "[message sent] " +
            data.message.owner.name +
            ": " +
            data.message.content
        );
      }
    };
  }

  inviteMember() {
    // TODO: show the invite page.
  }

  exit() {
    // TODO: show the exit page.
  }

  render() {
    const { msgs } = this.state;
    return (
      <div id="main">
        <div className="site-page-header-ghost-wrapper">
          <PageHeader
            ghost={false}
            title={this.context.roomName}
            subTitle={this.context.username}
            extra={[
              <Button key="invite" onClick={this.inviteMember}>Invite Member</Button>,
              <Button key="exit" onClick={this.exit}>Exit</Button>,
            ]}
          ></PageHeader>
        </div>
        <Card>
          <List
            className="msg-list"
            itemLayout="horizontal"
            dataSource={msgs}
            renderItem={(item) => <MsgEntry item={item} />}
          />
          <Divider />
          <SendMsg submitting={this.state.submitting} />
        </Card>
      </div>
    );
  }
}

export default ChatPage;
