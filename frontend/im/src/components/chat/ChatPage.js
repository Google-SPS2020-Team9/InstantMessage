import React from "react";
import {Button, Card, Divider, List, PageHeader} from "antd";
import MsgEntry from "./MsgEntry";
import SendMsg from "./SendMsg";
import {Context} from "../../context/ContextSource";
import "./ChatPage.css";

class ChatPage extends React.Component {
  state = {
    msgs: []
  };

  static contextType = Context;

  componentDidMount() {
    this.context.conn.addHandler("push messages", (data) => {
      for (const msg of data.messages) {
        this.setState((prevState) => ({
          msgs: [...prevState.msgs, msg],
        }));
        console.log(this.state.msgs);
        console.log(
          "[message received] " + msg.owner.name + ": " + msg.content
        );
      }
    });
  }

  inviteMember() {
    // TODO: show the invite page.
  }

  exit() {
    // TODO: show the exit page.
  }

  render() {
    if (!this.context.userid) return null;
    const {msgs} = this.state;
    return (
      <div id="main">
        <div className="site-page-header-ghost-wrapper">
          <PageHeader
            ghost={false}
            title={this.context.roomName}
            subTitle={this.context.username}
            extra={[
              <Button key="invite" onClick={this.inviteMember}>
                Invite Member
              </Button>,
              <Button key="exit" onClick={this.exit}>
                Exit
              </Button>,
            ]}
          />
        </div>
        <Card>
          <List
            className="msg-list"
            itemLayout="horizontal"
            dataSource={msgs}
            renderItem={(item) => <MsgEntry item={item}/>}
          />
          <Divider/>
          <SendMsg/>
        </Card>
      </div>
    );
  }
}

export default ChatPage;
