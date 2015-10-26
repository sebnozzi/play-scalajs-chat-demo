package com.sebnozzi.scalajs.example.chatclient.communication.angular

import com.greencatsoft.angularjs.{Service, injectable, Factory}
import com.sebnozzi.scalajs.example.chatclient.communication.ChatClient
import shared.ChatMsg


@injectable("msgChatClient")
class AngularChatClientFactory extends Factory[AngularChatClient] {
  override def apply() = {
    new AngularChatClient()
  }
}

@injectable("msgChatClient")
class AngularChatClient extends Service {

  var onMessageCallback: (ChatMsg) => Unit = (msg: shared.ChatMsg) => {
    println("Default impl. - does nothing")
  }

  // Introduce an indirection to avoid eager binding
  private val wsClient = new ChatClient(onMessage = (msg: shared.ChatMsg) => {
    onMessageCallback.apply(msg)
  })

  def sendMsg(msg: shared.ChatMsg): Unit = {
    wsClient.sendMsg(msg)
  }

  def connect(): Unit = wsClient.connect()

}


