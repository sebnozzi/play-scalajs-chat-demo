package controllers

import actors.ChatClientActor
import play.api.Play.current
import play.api.Logger
import play.api.mvc._
import actors.ChatServer
import play.api.libs.json._
import upickle.default._


object ChatController extends Controller {

  // Taken from https://www.playframework.com/documentation/2.4.x/ScalaWebSockets
  def connectToSocket = WebSocket.acceptWithActor[String, String] { request =>
    out =>
      Logger.debug("Creating web-socket")
      ChatClientActor.props(out)
  }

  def postMsg = Action { request =>
    request.body.asJson.fold(ifEmpty = BadRequest("Missing or invalid JSON")) {
      jsValue => processPostedJson(jsValue)
    }
  }

  def processPostedJson(jsValue: JsValue) = {
    val jsonStr = Json.stringify(jsValue)
    Logger.debug(s"Scala.js says: $jsonStr")
    try {
      val msgTypedEvt = read[shared.MessageTypedEvent](jsonStr)
      val chatMsg = msgTypedEvt.msg
      ChatServer.instance ! actors.BroadcastCmd(chatMsg)
      Ok(s"Play got -> $jsonStr via POST")
    } catch {
      case e: upickle.Invalid =>
        BadRequest(s"Incoming JSON could not be de-pickled: $jsonStr")
    }
  }

}