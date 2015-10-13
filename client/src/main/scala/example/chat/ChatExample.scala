package example.chat


object ChatExample {

  private var _client: ChatClient = _
  private var _ui: ChatUI = _
  // Necessary due to cyclic dependency
  private def ui: ChatUI = _ui
  private def client: ChatClient = _client

  def doIt(): Unit = {
    _client = new ChatClient(onMessage = addMsgToUI)
    _ui = new ChatUI(onUserInput = sendMsgToServer)
  }

  private def addMsgToUI(msg: shared.ChatMsg): Unit = {
    ui.showMsg(msg)
  }

  private def sendMsgToServer(userText: String): Unit = {
    val chatMsg = shared.ChatMsg(userText)
    
    if(ui.isPostSendingSelected)
      AjaxChatMsgPoster.postMsg(chatMsg)
    else
      client.sendMsg(chatMsg)
  }

}