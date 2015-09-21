package example.chat

import shared.SharedMessages
import org.scalajs.jquery._
import upickle.default._

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

  private def addMsgToUI(msg: SharedMessages.ChatMsg): Unit = {
    ui.showMsg(msg)
  }

  private def sendMsgToServer(userText: String): Unit = {
    val chatMsg = SharedMessages.ChatMsg(userText)
    // client.sendMsg(chatMsg)
    AjaxChatMsgPoster.postMsg(chatMsg)
  }

}