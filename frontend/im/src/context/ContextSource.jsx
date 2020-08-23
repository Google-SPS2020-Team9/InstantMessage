import React from 'react'
import PropTypes from 'prop-types'

export const Context = React.createContext()

export class ContextProvider extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      username: null,
      userid: null,
      roomName: null,
      roomid: null,
      selectRmModalVisibility: true,
      signInModalVisibility: false
    }
  }

  static get propTypes () {
    return {
      children: PropTypes.any
    }
  }

  // SelectRmModal related
  setSelectRmModalVisibility = (v) => {
    this.setState({
      selectRmModalVisibility: v
    })
  };

  showSelectRmModal = () => {
    this.setSelectRmModalVisibility(true)
  };

  closeSelectRmModal = () => {
    this.setSelectRmModalVisibility(false)
  };

  // SignInModal related
  setSignInModalVisibility = (v) => {
    this.setState({
      signInModalVisibility: v
    })
  };

  showSignInModal = () => {
    this.setSignInModalVisibility(true)
  };

  closeSignInModal = () => {
    this.setSignInModalVisibility(false)
  };

  // User related
  setUserName = (username) => {
    this.setState({ username: username })
  };

  setUserId = (userid) => {
    this.setState({ userid: userid })
  };

  // Room related
  setRoomName = (room) => {
    this.setState({ roomName: room })
  };

  setRoomId = (roomid) => {
    this.setState({ roomid: roomid })
  };

  // Connection related
  setConn = (conn) => {
    this.setState({ conn: conn })
  };

  render () {
    return (
      <Context.Provider
        value={{
          username: this.state.username,
          userid: this.state.userid,
          roomName: this.state.roomName,
          conn: this.state.conn,
          selectRmModalVisibility: this.state.selectRmModalVisibility,
          signInModalVisibility: this.state.signInModalVisibility,
          setUserName: this.setUserName,
          setUserId: this.setUserId,
          setRoomName: this.setRoomName,
          setRoomId: this.setRoomId,
          setConn: this.setConn,
          showSelectRmModal: this.showSelectRmModal,
          closeSelectRmModal: this.closeSelectRmModal,
          showSignInModal: this.showSignInModal,
          closeSignInModal: this.closeSignInModal
        }}
      >
        {this.props.children}
      </Context.Provider>
    )
  }
}
