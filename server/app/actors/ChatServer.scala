package actors

import play.api.Play.current
import play.api.libs.concurrent.Akka
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef
import scala.collection.mutable

object ChatServer {
  lazy val instance: ActorRef = Akka.system.actorOf(Props[ChatServer], name = "chat_server_actor")

  case class Login()
  case class Logout()
  case class Broadcast(msg: String)
  case class ChatMsg(msg: String)
}

class ChatServer extends Actor {
  import ChatServer._

  private val users = mutable.Set[ActorRef]()

  override def receive = {
    case Login() =>
      users += sender
      println("Logged-in user")
    case Logout() =>
      users -= sender
      println("Logged-out user")
    case Broadcast(msg) =>
      for (user <- users) {
        user ! ChatMsg(msg)
      }
  }

}