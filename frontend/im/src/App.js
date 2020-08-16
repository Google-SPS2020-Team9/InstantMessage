import React from "react";
import SelectRmModal from "./components/authModal/SelectRmModal";
import SignInModal from "./components/authModal/SignInModal";

import { ContextProvider } from "./context/ContextSource";
import MainBg from "./components/chat/MainBg";

class App extends React.Component {
  render() {
    return (
      <ContextProvider>
        <SelectRmModal />
        <SignInModal />
        <MainBg />
      </ContextProvider>
    );
  }
}

export default App;
