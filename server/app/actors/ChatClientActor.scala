package actors

import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import upickle.default._
import play.api.Logger

object ChatClientActor {
  def chatServer = ChatServer.instance

  def props(webClient: ActorRef) = Props(new ChatClientActor(webClient, chatServer))
}

/**
 * This is created for each chat-client connected to the
 * server (Play application) via websocket.
 **/
class ChatClientActor(webClient: ActorRef, chatServer: ActorRef) extends Actor {

  override def preStart() = chatServer ! LoginCmd()
  override def postStop() = chatServer ! LogoutCmd()

  def receive = {

    case incomingStringFromBrowser: String => 
      convertAndResend(incomingStringFromBrowser)

    case msgTypedEvt: shared.MessageTypedEvent =>
      val chatMsg = msgTypedEvt.msg
      // Tell the server to broadcast the message
      chatServer ! BroadcastCmd(chatMsg)

    // Sends message back to the web-client
    case showMsgCmd: shared.ShowMessageCmd =>
      val jsonStr = write(showMsgCmd)
      webClient ! jsonStr
  }

  private def convertAndResend(incomingStringFromBrowser: String): Unit = {
    Logger.debug(s"Got via WebSocket: $incomingStringFromBrowser")
    try {
      val msgTypedEvt = read[shared.MessageTypedEvent](incomingStringFromBrowser)
      self ! msgTypedEvt
    } catch {
      case e: upickle.Invalid =>
        Logger.error(s"Could not de-pickle incoming JSON: $incomingStringFromBrowser")
    }
  }

}