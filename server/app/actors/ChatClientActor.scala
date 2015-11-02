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
 *
 * Note how each of these actors is passed a web-socket Actor.
 *
 * The web-socket Actor is created by Play and handles the web-socket
 * communication itself. The Actor implemented by this class deals
 * with what to DO with the communication going on in the web-socket.
 *
 * The web-socket Actor will forward plain/raw String messages to
 * this Actor, and when we want to send plain/raw Strings back,
 * we will send them to the web-socket Actor, which will forward
 * these to the browser.
 *
 * Internally, however, we want to convert to Scala-objects (case-classes)
 * and deal with these as soon and as much as possible.
 *
 **/
class ChatClientActor(webClient: ActorRef, chatServer: ActorRef) extends Actor {

  /**
   * The "preStart" method is called in the Akka-Actor-lifecycle when
   * the Actor has been created and just before it is ready to begin
   * processing its message-queue.
   *
   * It is a good opportunity to "log-in" to the chat-server by
   * letting it know about our existence.
   *
   * No need to send an ActorRef of ourselves, since the receiving
   * Actor can know that through the "sender" method.
   */
  override def preStart() = chatServer ! LoginCmd()

  /**
   * When the web-socket is closed, Play will terminate its web-socket
   * Actor and this associated one.
   *
   * When an Actor is about to finish, the "postStop" method is invoked
   * to do some cleanup. In this opportunity, we "log-out" from the
   * chat-server by sending, again, a message about that fact.
   *
   * The chat-server will know its us through the "sender" method.
   * */
  override def postStop() = chatServer ! LogoutCmd()

  def receive = {

    // Incoming raw-String from the associated web-socket Actor
    case incomingStringFromBrowser: String =>
      deserializeAndResendToOurselves(incomingStringFromBrowser)

    case msgTypedEvt: shared.MessageTypedEvent =>
      val chatMsg = msgTypedEvt.msg
      if(!chatMsg.isEmpty) {
        // Tell the server to broadcast the message
        chatServer ! BroadcastCmd(chatMsg)
      }

    // Sends message back to the web-client
    case showMsgCmd: shared.ShowMessageCmd =>
      val jsonStr = write(showMsgCmd)
      webClient ! jsonStr
  }

  // The String is supposed to be a serialized Scala-Object using JSON
  private def deserializeAndResendToOurselves(incomingStringFromBrowser: String): Unit = {
    Logger.debug(s"Got via WebSocket: $incomingStringFromBrowser")
    try {
      val msgTypedEvt = read[shared.MessageTypedEvent](incomingStringFromBrowser)
      self ! msgTypedEvt
    } catch {
      case e: upickle.Invalid =>
        Logger.error(s"Could not de-serialize incoming String: $incomingStringFromBrowser")
    }
  }

}