package actors

import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import upickle.default._
import shared.SharedMessages

object ChatUserActor {
  def chatServer = ChatServer.instance
  def props(webClient: ActorRef) = Props(new ChatUserActor(webClient, chatServer))
}

class ChatUserActor(webClient: ActorRef, chatServer:ActorRef) extends Actor {

  override def preStart() = chatServer ! ChatServer.Login()
  override def postStop() = chatServer ! ChatServer.Logout()
  
  def receive = {
    case rawString : String => 
      // This could fail...
      val chatMsg = read[SharedMessages.ChatMsg](rawString)
      chatServer ! ChatServer.Broadcast(chatMsg.txt)
    case ChatServer.ChatMsg(msg) =>
      webClient ! msg
  }


  
}