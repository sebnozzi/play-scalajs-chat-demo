package controllers

import actors.ChatUserActor
import play.api.Play.current
import play.api.mvc._
import actors.ChatServer
import play.api.libs.json._
import upickle.default._
import shared.SharedMessages

object ChatController extends Controller {

  // Taken from https://www.playframework.com/documentation/2.4.x/ScalaWebSockets
  def socket = WebSocket.acceptWithActor[String, String] { request =>
    out =>
      ChatUserActor.props(out)
  }

  def postMsg = Action { request =>
    request.body.asJson.fold(ifEmpty = {
      BadRequest("Missing or invalid JSON")
    }) { jsValue =>
      val jsonStr = Json.stringify(jsValue)
      println(s"Scala.js says: jsonStr")
      try {
        val chatMsg = read[SharedMessages.ChatMsg](jsonStr)
        ChatServer.instance ! ChatServer.Broadcast(chatMsg.txt)
        Ok(s"Play got -> $jsonStr via POST")
      } catch {
        case e: upickle.Invalid =>
          BadRequest(s"Incoming JSON could not be de-pickled: $jsonStr")
      }
    }
  }

}