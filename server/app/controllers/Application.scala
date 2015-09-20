package controllers

import play.api.mvc._
import shared.SharedMessages

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(SharedMessages.itWorks))
  }
  
  def sendMsg = Action { request =>
    val msg = request.body.asText.getOrElse("")
    Ok(s"got: $msg")
  }

}
