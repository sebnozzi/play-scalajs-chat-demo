package controllers

import actors.{ChatClientActor, ChatServer}
import play.api.Logger
import play.api.Play.current
import play.api.libs.json._
import play.api.mvc._
import upickle.default._

object ChatController extends Controller {

  // Taken from https://www.playframework.com/documentation/2.4.x/ScalaWebSockets
  def connectToSocket = WebSocket.acceptWithActor[String, String] { request =>
    out =>
      Logger.debug("Creating web-socket")
      ChatClientActor.props(out)
  }

  def postMsg = Action(parse.json) { request =>
    processPostedJson(request.body)
  }

  private def processPostedJson(jsValue: JsValue): Result = {
    val jsonStr = Json.stringify(jsValue)
    Logger.debug(s"Scala.js says: $jsonStr")

    try {

      val msgTypedEvt = read[shared.MessageTypedEvent](jsonStr)
      val chatMsg = msgTypedEvt.msg

      if(!chatMsg.isEmpty) {
        ChatServer.instance ! actors.BroadcastCmd(chatMsg)
      }

      val responseJsonStr = write(shared.MessageReceived())
      Ok(responseJsonStr).as(JSON)

    } catch {
      case e: upickle.Invalid =>
        BadRequest(s"Incoming JSON could not be de-pickled: $jsonStr")
    }
  }

}