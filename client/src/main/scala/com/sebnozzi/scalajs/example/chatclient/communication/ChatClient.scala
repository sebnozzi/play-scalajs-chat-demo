package com.sebnozzi.scalajs.example.chatclient.communication

import com.sebnozzi.scalajs.communication.serialization.JsonSerialization
import com.sebnozzi.scalajs.communication.websocket.WebSocketClient
import shared.ChatMsg
import upickle.default._

import scala.util.Try

/**
 * Connects to the chat-server via web-socket.
 *
 * Sends message-typed-events to the server and processes
 * show-message-commands sent back from the server.
 *
 * */
class ChatClient(onMessage: (shared.ChatMsg) => Any) {

  private object MySocketClient
    extends WebSocketClient[shared.MessageTypedEvent, shared.ShowMessageCmd]
    with ChatClientSerialization {

    override lazy val relativeSocketUrl = ServerURLs.socketURL

    override def receive(showMsgCmd: shared.ShowMessageCmd): Unit = {
      onMessage(showMsgCmd.msg)
    }
  }

  def connect() = MySocketClient.connect()

  def sendMsg(chatMsg: ChatMsg): Unit = {
    MySocketClient.send(shared.MessageTypedEvent(chatMsg))
  }

}

trait ChatClientSerialization extends JsonSerialization[shared.MessageTypedEvent, shared.ShowMessageCmd] {

  override protected val jsonWriter =
    (o: shared.MessageTypedEvent) => write(o)

  override protected val jsonReader =
    (s: String) => Try(read[shared.ShowMessageCmd](s))

}