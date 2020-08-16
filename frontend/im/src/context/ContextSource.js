import React from "react";
import UserConfig from "../testData/userconfig.json";

export const Context = React.createContext();

export class ContextProvider extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: null,
      userid: null,
      roomName: null,
      roomid: null,
      host: null,
      selectRmModalVisibility: true,
      signInModalVisibility: false,
    };
  }

  componentDidMount() {
    const userconfig = UserConfig;
    this.setState({
      host: "gogo.moe:8080",
      username: userconfig.username,
      userid: userconfig.userid,
      roomName: userconfig.roomname,
      roomid: userconfig.roomid,
    });
  }

  // SelectRmModal related
  setSelectRmModalVisibility = (v) => {
    this.setState({
      selectRmModalVisibility: v,
    });
  };
  showSelectRmModal = () => {
    this.setSelectRmModalVisibility(true);
  };
  closeSelectRmModal = () => {
    this.setSelectRmModalVisibility(false);
  };

  // SignInModal related
  setSignInModalVisibility = (v) => {
    this.setState({
      signInModalVisibility: v,
    });
  };
  showSignInModal = () => {
    this.setSignInModalVisibility(true);
  };
  closeSignInModal = () => {
    this.setSignInModalVisibility(false);
  };

  // User related
  setUserName = (username) => {
    this.setState({ username: username });
  };
  setUserId = (userid) => {
    this.setState({ userid: userid });
  };

  // Room related
  setRoomName = (room) => {
    this.setState({ roomName: room });
  };

  setRoomId = (roomid) => {
    this.setState({ roomid: roomid });
  };

  // Connection related
  setHost = (host) => {
    this.setState({ host: host });
  };
  setConn = (conn) => {
    this.setState({ conn: conn });
  };

  render() {
    return (
      <Context.Provider
        value={{
          username: this.state.username,
          userid: this.state.userid,
          roomName: this.state.roomName,
          host: this.state.host,
          conn: this.state.conn,
          selectRmModalVisibility: this.state.selectRmModalVisibility,
          signInModalVisibility: this.state.signInModalVisibility,
          setUserName: this.setUserName,
          setUserId: this.setUserId,
          setRoomName: this.setRoomName,
          setRoomId: this.setRoomId,
          setHost: this.setHost,
          setConn: this.setConn,
          showSelectRmModal: this.showSelectRmModal,
          closeSelectRmModal: this.closeSelectRmModal,
          showSignInModal: this.showSignInModal,
          closeSignInModal: this.closeSignInModal,
        }}
      >
        {this.props.children}
      </Context.Provider>
    );
  }
}
