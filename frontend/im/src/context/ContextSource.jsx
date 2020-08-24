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
      roomid: null
    }
  }

  static get propTypes () {
    return {
      children: PropTypes.any
    }
  }

  setUserName = (username) => {
    this.setState({ username: username })
  };

  setUserId = (userid) => {
    this.setState({ userid: userid })
  };

  setRoomName = (room) => {
    this.setState({ roomName: room })
  };

  setRoomId = (roomid) => {
    this.setState({ roomid: roomid })
  };

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
          roomid: this.state.roomid,
          conn: this.state.conn,
          setUserName: this.setUserName,
          setUserId: this.setUserId,
          setRoomName: this.setRoomName,
          setRoomId: this.setRoomId,
          setConn: this.setConn
        }}
      >
        {this.props.children}
      </Context.Provider>
    )
  }
}
