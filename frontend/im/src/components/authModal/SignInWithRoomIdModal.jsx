import {Context} from '../../context/ContextSource'
import {Connection} from "../../context/Connection";
import {message} from "antd";
import SignInModal from "./SignInModal";

class SignInWithRoomIdModal extends SignInModal {
  static contextType = Context;

  constructor (props) {
    super(props)
    this.state = {
      activeTab: 'enter',
      roomid: props.match.params.roomid,
      roomName: ''
    }
  }

  componentDidMount = () => {
    console.log('SelectRmModal::handleEnterRoom')
    if (this.state.roomid === '') return

    const connection = new Connection(this.state.roomid)
    this.context.setConn(connection)
    connection.addHandler('room state', (data) => {
      if (data.success === true) {
        console.log(
          '[room state]: joining successful. Room id: ' + data.room.id
        )
        this.context.setRoomId(data.room.id)
        this.context.setRoomName(data.room.name)
        this.context.closeSelectRmModal()
        this.context.showSignInModal()
        this.goSignInPage(this, data.room.id)
      } else {
        console.log('[room state]: joining unsuccessful.')
        this.context.setConn(null)
        if (data.error === 'Room not exist') {
          message.error("Room doesn't exist.")
        } else {
          message.error('An error occurred. Please try again.')
          console.error(data)
        }
        this.goHomePage(this)
      }
    })
  }

  goHomePage = () => {
    this.props.history.push('/')
  }

  goChatPage = () => {
    this.props.history.push('/room/chat/' + this.context.roomid)
  }

  goSignInPage = () => {
    this.props.history.push('/room')
  }
}

export default SignInWithRoomIdModal
