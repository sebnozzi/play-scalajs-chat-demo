package controllers

import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef

object MyWebSocketActor {
  def props(out: ActorRef) = Props(new MyWebSocketActor(out))
}

class MyWebSocketActor(out: ActorRef) extends Actor {

  def receive = {
    case msg: String =>
      out ! ("I received your message: " + msg)
  }

  override def postStop() = {
    println("The websocket has been closed")
  }
}