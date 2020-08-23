import React from 'react'
import { Context } from '../../context/ContextSource'
import PropTypes from 'prop-types'
import './MsgEntry.css'

class MsgEntry extends React.Component {
  constructor (props) {
    super(props)
    this.state = {}
  }

  static contextType = Context;

  static get propTypes () {
    return {
      item: PropTypes.any
    }
  }

  render () {
    const msg = this.props.item
    const isUserMsg = this.props.item.owner.id === this.context.userid

    return (
      <div className={'msg-wrapper ' + (isUserMsg ? 'outgoing' : 'incoming')}>
        <div className='msg-name'>{msg.owner.name}</div>
        <div className='msg-content'>{msg.content}</div>
      </div>
    )
  }
}

export default MsgEntry
