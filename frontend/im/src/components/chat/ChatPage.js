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
          this.state.msgs.push(msg);
          console.log(
            "[message received] " + msg.owner.name + ": " + msg.content
          );
        }
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
    console.log(msgs);
    return (
      <div id="main">
        <div className="site-page-header-ghost-wrapper">
          <PageHeader
            ghost={false}
            title={this.context.roomName}
            subTitle={this.context.username}
            extra={[
              <Button onClick={this.inviteMember}>Invite Member</Button>,
              <Button onClick={this.exit}>Exit</Button>,
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
          <SendMsg />
        </Card>
      </div>
    );
  }
}

export default ChatPage;
