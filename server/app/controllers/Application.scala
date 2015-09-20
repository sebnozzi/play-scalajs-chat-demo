package controllers

import play.api.mvc._
import shared.SharedMessages
import play.api.Play.current
import akka.actor._
import actors.MyWebSocketActor


object Application extends Controller {

  def index = Action {
    Ok(views.html.index(SharedMessages.itWorks))
  }

  def sendMsg = Action { request =>
    val msg = request.body.asText.getOrElse("")
    println(s"Scala.js says: $msg")
    Ok(s"<<Play got: $msg>>")
  }

  // Taken from https://www.playframework.com/documentation/2.4.x/ScalaWebSockets

  def socket = WebSocket.acceptWithActor[String, String] { request => out =>
    MyWebSocketActor.props(out)
  }

}
