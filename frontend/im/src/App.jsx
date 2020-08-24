import React from 'react'
import { Route } from 'react-router-dom'
import SelectRmModal from './components/authModal/SelectRmModal'
import SignInWithRoomIdModal from './components/authModal/SignInWithRoomIdModal'

import { ContextProvider } from './context/ContextSource'
import SignInModal from './components/authModal/SignInModal'
import ChatPage from './components/chat/ChatPage'

class App extends React.Component {
  render () {
    return (
      <ContextProvider>
        {<Route path='/' exact component={SelectRmModal} />}
        {<Route path='/room' exact component={SignInModal} />}
        {<Route path='/room/:roomid' exact component={SignInWithRoomIdModal} />}
        {<Route path='/room/chat/:roomid' exact component={ChatPage} />}
      </ContextProvider>
    )
  }
}

export default App
