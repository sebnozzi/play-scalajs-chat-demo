package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Redirect(routes.Application.jQueryIndex())
  }

  def jQueryIndex = Action {
    Ok(views.html.index(usingAngular = false))
  }

  def angularIndex = Action {
    Ok(views.html.index(usingAngular = true))
  }

}
