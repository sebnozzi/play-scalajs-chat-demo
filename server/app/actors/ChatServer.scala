package actors

import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.Logger
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef
import scala.collection.mutable

object ChatServer {
  lazy val instance: ActorRef = Akka.system.actorOf(Props[ChatServer], name = "chat_server_actor")
}

class ChatServer extends Actor {

  private val chatClients = mutable.Set[ActorRef]()

  override def receive = {

    case LoginCmd() =>
      chatClients += sender
      Logger.debug("Logged-in user")

    case LogoutCmd() =>
      chatClients -= sender
      Logger.debug("Logged-out user")

    case BroadcastCmd(msg) =>
      for (chatClient <- chatClients)
        chatClient ! shared.ShowMessageCmd(msg)

  }

}