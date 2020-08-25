import React from 'react'
import { Route, Switch } from 'react-router-dom'
import SelectRmModal from './components/authModal/SelectRmModal'

import { ContextProvider } from './context/ContextSource'
import SignInModal from './components/authModal/SignInModal'
import ChatPage from './components/chat/ChatPage'

class App extends React.Component {
  render () {
    return (
      <ContextProvider>
        <Switch>
          <Route path='/' exact component={SelectRmModal} />
          <Route path='/room/:roomid' exact component={SignInModal} />
          <Route path='/room/chat/:roomid' exact component={ChatPage} />
        </Switch>
      </ContextProvider>
    )
  }
}

export default App
