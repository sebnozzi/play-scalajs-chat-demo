package com.sebnozzi.scalajs.example.chat

import scala.scalajs.js

/**
 * Created by sebnozzi on 14/10/15.
 */
object ChatExample extends js.JSApp {

  private var _client: ChatClient = _
  private var _ui: ChatUI = _

  // Necessary due to cyclic dependency
  private def ui: ChatUI = _ui

  private def client: ChatClient = _client

  def main(): Unit = {
    _client = new ChatClient(onMessage = addMsgToUI)
    _ui = new ChatUI(onUserInput = sendMsgToServer)
  }

  private def addMsgToUI(msg: shared.ChatMsg): Unit = {
    ui.addChatMsg(msg)
  }

  private def sendMsgToServer(userText: String): Unit = {
    val chatMsg = shared.ChatMsg(userText)

    if (ui.isPostSendingSelected)
      AjaxChatMsgPoster.postMsg(chatMsg)
    else
      client.sendMsg(chatMsg)
  }

}
