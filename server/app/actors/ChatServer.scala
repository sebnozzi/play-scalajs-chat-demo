package actors

import play.api.Play.current
import play.api.libs.concurrent.Akka
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef
import scala.collection.mutable

object ChatServer {
  lazy val instance: ActorRef = Akka.system.actorOf(Props[ChatServer], name = "chat_server_actor")

  case class Login(user: ActorRef)
  case class Logout(user: ActorRef)
  case class Broadcast(sender: ActorRef, msg: String)
}

class ChatServer extends Actor {
  import ChatServer._

  private val users = mutable.Set[ActorRef]()

  override def receive = {
    case Login(user) =>
      users += user
    case Logout(user) =>
      users -= user
    case Broadcast(sender, msg) =>
      for (otherUser <- users.filterNot(_ == sender)) {
        otherUser ! msg
      }
  }

}