package com.sebnozzi.scalajs.example.chatclient.ui.angular

import com.greencatsoft.angularjs.core.HttpService
import com.greencatsoft.angularjs.{inject, Controller, injectable}
import com.sebnozzi.scalajs.example.chatclient.communication.angular.{AngularChatClient, AngularMsgPostService}
import shared.ChatMsg

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport


@injectable("chatCtl")
object ChatCtrl extends Controller[ChatScope]  {

  @inject
  var service : AngularMsgPostService = _

  @inject
  var chatClient : AngularChatClient = _

  @inject
  var scope : ChatScope = _

  override def initialize() {
    super.initialize()
    initScope()
    initChatClient()
  }

  private def initScope(): Unit = {
    scope.messages = js.Array[String]()
    scope.typedMessage = ""
    scope.sendMethod = "post"
  }

  def initChatClient(): Unit = {
    chatClient.onMessageCallback = addChatMsg
    chatClient.connect()
  }

  @JSExport
  def sendMsg():Unit = {
    if(isPostSendingSelected) {
      sendMsgViaPost
    } else {
      sendMsgViaWebSocket
    }
    scope.typedMessage = ""
  }

  private def sendMsgViaWebSocket: Any = {
    chatClient.sendMsg(typedChatMessage)
  }

  private def sendMsgViaPost: Unit = {
    service.postMessage(typedChatMessage)
  }

  private def typedChatMessage = shared.ChatMsg(txt = scope.typedMessage)

  def addChatMsg(msg: ChatMsg): Unit = {
    // We have to call $apply because this call-chain is
    // initiated outside of Angular's digest-cycle.
    // With this, we tell Angular that the result of this
    // operation changes its state, and that it has to reflect
    // changes in the UI (at least potentially).
    scope.$apply({
      val textToAdd = msg.txt
      scope.messages.unshift(textToAdd)
    })
  }

  def isPostSendingSelected: Boolean = {
    scope.sendMethod == "post"
  }
}

