import React from 'react'
import { Context } from '../../context/ContextSource'
import ChatPage from './ChatPage'
import SelectRmModal from "../authModal/SelectRmModal";

class MainBg extends React.Component {
  static contextType = Context;

  render () {
    return this.context.conn ? <ChatPage /> : <SelectRmModal />
  }
}

export default MainBg
