package actors

import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.actorRef2Scala

object ChatUserActor {
  def chatServer = ChatServer.instance
  def props(webClient: ActorRef) = Props(new ChatUserActor(webClient, chatServer))
}

class ChatUserActor(webClient: ActorRef, chatServer:ActorRef) extends Actor {

  override def preStart() = chatServer ! ChatServer.Login()
  override def postStop() = chatServer ! ChatServer.Logout()
  
  def receive = {
    case rawMsg : String => 
      chatServer ! ChatServer.Broadcast(rawMsg)
    case ChatServer.ChatMsg(msg) =>
      webClient ! msg
  }


  
}