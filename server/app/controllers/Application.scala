package controllers

import play.api.mvc._
import shared.SharedMessages
import play.api.Logger

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(SharedMessages.itWorks))
  }
  
  def sendMsg = Action { request =>
    val msg = request.body.asText.getOrElse("")
    println(s"Scala.js says: $msg")
    Ok(s"<<Play got: $msg>>")
  }

}
