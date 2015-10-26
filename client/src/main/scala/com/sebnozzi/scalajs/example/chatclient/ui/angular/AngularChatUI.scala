package com.sebnozzi.scalajs.example.chatclient.ui.angular

import com.sebnozzi.scalajs.example.chatclient.ui.ChatUI
import shared.ChatMsg

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

import com.greencatsoft.angularjs.{Controller, Angular, injectable, inject}
import com.greencatsoft.angularjs.core.{Scope}

/**
 * Created by sebnozzi on 26/10/15.
 */
class AngularChatUI(messageTypedCallback: (String) => Any) extends ChatUI {

  initAngular()

  def initAngular(): Unit = {
    val module = Angular.module("chat-ui")

    module
      .controller(ChatCtrl)

    // Inject this outside of Angular (can we do it through Angular?)
    ChatCtrl.messageTypedCallback = messageTypedCallback
  }

  override def addChatMsg(msg: ChatMsg): Unit = ChatCtrl.addChatMsg(msg)

  override def isPostSendingSelected: Boolean = ChatCtrl.isPostSendingSelected
}

trait ChatScope extends Scope {
  var messages: js.Array[String] = js.native
  var typedMessage: String = js.native
  var sendMethod: String = js.native
}

@injectable("chatCtl")
object ChatCtrl
  extends Controller[ChatScope] with ChatUI {

  @inject
  var scope : ChatScope = _

  var messageTypedCallback: (String) => Any = (s:String) => {}

  override def initialize() {
    super.initialize()
    initScope()
  }

  private def initScope(): Unit = {
    scope.messages = {
      val arr = js.Array[String]()
      arr
    }
    scope.typedMessage = ""
    scope.sendMethod = "post"
  }

  @JSExport
  def sendMsg():Unit = {
    messageTypedCallback.apply(typedMessage)
    scope.typedMessage = ""
  }

  private def typedMessage:String = scope.typedMessage

  override def addChatMsg(msg: ChatMsg): Unit = {
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

  override def isPostSendingSelected: Boolean = {
    scope.sendMethod == "post"
  }
}
