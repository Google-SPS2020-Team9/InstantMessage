import React from 'react'
import { Context } from '../../context/ContextSource'
import ChatPage from './ChatPage'

class MainBg extends React.Component {
  static contextType = Context;

  render () {
    return this.context.conn ? <ChatPage /> : null
  }
}

export default MainBg
