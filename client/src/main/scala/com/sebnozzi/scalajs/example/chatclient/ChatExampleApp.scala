package com.sebnozzi.scalajs.example.chatclient

import com.sebnozzi.scalajs.example.chatclient.communication.{ChatClient, ChatMsgPoster}
import com.sebnozzi.scalajs.example.chatclient.ui.{ChatUIFactory, ChatUI}
import com.sebnozzi.scalajs.example.chatclient.ui.angular.AngularChatUI
import com.sebnozzi.scalajs.example.chatclient.ui.jquery.JQueryChatUI

import scala.scalajs.js

/**
 * Simple chat-application example.
 *
 * This is the client part, implemented in Scala.js
 *
 * It illustrates sending and receiving serialized
 * Scala-objects (to/from JSON) to and from the server.
 *
 * It communicates either via web-sockets or AJAX
 * (POST) requests.
 *
 */
object ChatExampleApp extends js.JSApp {

  private var client: ChatClient = _
  private var ui: ChatUI = _

  def main(): Unit = {
    client = new ChatClient(onMessage = addMsgToUI)
    ui = ChatUIFactory.buildInstance(messageTypedCallback = sendMsgToServer)

    client.connect()
  }

  private def addMsgToUI(msg: shared.ChatMsg): Unit = {
    ui.addChatMsg(msg)
  }

  private def sendMsgToServer(userText: String): Unit = {
    val chatMsg = shared.ChatMsg(userText)

    if (ui.isPostSendingSelected)
      ChatMsgPoster.postMsg(chatMsg)
    else
      client.sendMsg(chatMsg)
  }

}
