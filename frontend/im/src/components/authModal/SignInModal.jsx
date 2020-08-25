import React from 'react'
import { Button, Form, Input, message, Modal } from 'antd'
import { Context } from '../../context/ContextSource'

import './AuthModal.css'
import PropTypes from 'prop-types'
import { Connection } from '../../context/Connection'

class SignInModal extends React.Component {
  static contextType = Context;

  constructor (props) {
    super(props)
    this.wrapper = React.createRef()
    this.state = {
      userName: null,
      roomId: props.match.params.roomid
    }
  }

  static get propTypes () {
    return {
      history: PropTypes.any,
      match: PropTypes.any
    }
  }

  componentDidMount = () => {
    if (this.context.conn) {
      this.context.conn.close()
      this.context.setConn(null)
    }
    const connection = new Connection(this.state.roomId)
    this.context.setConn(connection)
    connection.addHandler('room state', (data) => {
      if (data.success === true) {
        console.log(
          '[room state]: joining successful. Room id: ' + data.room.id
        )
        this.context.setRoomId(data.room.id)
        this.context.setRoomName(data.room.name)
      } else {
        console.log('[room state]: joining unsuccessful.')
        this.context.setConn(null)
        if (data.error === 'Room not exist') {
          message.error("Room doesn't exist.")
        } else {
          message.error('An error occurred. Please try again.')
          console.error(data)
        }
        this.goHomePage()
      }
    })
  }

  handleLogin = () => {
    console.log('SignInModal::handleLogin')
    console.log(this.state.userName)
    this.context.conn.request('sign in', { username: this.state.userName }).then(data => {
      this.context.setUserId(data.user.id)
      this.context.setUserName(data.user.name)
      message.success('Login successful as ' + this.context.username)
      console.log('[signing in] ' + data.user.id + ': ' + data.user.name)
      this.goChatPage()
    }).catch(data => {
      message.error('Login failed. Please try again.')
      console.error(data)
    })
  };

  handleUserName = (e) => {
    this.setState({ userName: e.target.value })
  };

  goHomePage = () => {
    this.props.history.push('/')
  }

  goChatPage = () => {
    this.props.history.push('/room/chat/' + this.context.roomid)
  }

  render () {
    const layout = {
      labelCol: { span: 0 },
      wrapperCol: { span: 16 }
    }
    const tailLayout = {
      wrapperCol: { offset: 0, span: 16 }
    }

    return (
      <Modal
        title='Enter your username'
        centered
        className='sign-in-modal'
        visible
        footer={null}
      >
        <p>
          You&#39;ve successfully entered room {this.context.roomName}. The next
          step is to have a username. Note that it cannot be the same with other
          room members. If leaving blank, you will be given a random user name.{' '}
        </p>
        <Form onFinish={this.handleLogin} {...layout}>
          <Form.Item label='UserName' name='username'>
            <Input value={this.state.userName} onChange={this.handleUserName} />
          </Form.Item>

          <Form.Item {...tailLayout}>
            <Button type='primary' htmlType='submit'>
              Start Chat
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    )
  }
}

export default SignInModal
