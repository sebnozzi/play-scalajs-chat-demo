package actors

import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.actorRef2Scala

object ChatUserActor {
  def props(out: ActorRef) = Props(new ChatUserActor(out))
}

class ChatUserActor(out: ActorRef) extends Actor {

  def receive = {
    case msg: String =>
      out ! msg
  }

  override def postStop() = {
    println("The websocket has been closed")
  }
}