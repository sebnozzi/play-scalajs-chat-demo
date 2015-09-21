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
    // Gets message FROM web-client
    case rawJsonString : String => 
      println(s"Got via WebSocket: $rawJsonString")
      // This could fail...
      val chatMsg = read[SharedMessages.ChatMsg](rawJsonString)
      // Tell the server to broadcast the message
      chatServer ! ChatServer.Broadcast(chatMsg.txt)
    
    // Sends message back to the web-client
    case chatMsg @ ChatServer.ChatMsg(txt) =>
      val jsonStr = write(SharedMessages.ChatMsg(txt))
      webClient ! jsonStr
  }


  
}