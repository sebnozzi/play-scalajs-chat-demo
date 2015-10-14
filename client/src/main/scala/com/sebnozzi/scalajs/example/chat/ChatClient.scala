package com.sebnozzi.scalajs.example.chat

import com.sebnozzi.scalajs.comm.WebSocketClient
import shared.ChatMsg
import upickle.default._

import scala.util.Try


class ChatClient(onMessage: (shared.ChatMsg) => Any)
  extends WebSocketClient[shared.MessageTypedEvent, shared.ShowMessageCmd] {

  override lazy val relativeSocketUrl = "/socket"

  override protected val jsonWriter = (o: shared.MessageTypedEvent) => write(o)
  override protected val jsonReader = (s: String) => Try(read[shared.ShowMessageCmd](s))

  override def receive(showMsgCmd: shared.ShowMessageCmd): Unit = {
    onMessage(showMsgCmd.msg)
  }

  def sendMsg(chatMsg: ChatMsg): Unit = {
    send(shared.MessageTypedEvent(chatMsg))
  }

}
