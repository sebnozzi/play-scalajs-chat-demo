package com.sebnozzi.scalajs.communication.ajax

import com.sebnozzi.scalajs.communication.serialization.JsonSerialization
import org.scalajs.jquery._

import scala.scalajs.js

/**
 * Abstract class.
 *
 * Performs POST-requests of specific types, awaiting responses of other
 * specific type.
 *
 * Concrete classes have to provide a way to serialize from Scala-types
 * to JSON and back.
 *
 * Also to be provided is the relative-URL to send the requests to.
 *
 */
abstract class AjaxPoster[TO_SERVER, FROM_SERVER] extends JsonSerialization[TO_SERVER, FROM_SERVER] {

  def postRelativeUrl: String

  protected def onSuccess(responseData: FROM_SERVER, textStatus: String): Unit = {}

  protected def onError(textStatus: String, errorThrow: String): Unit = {}

  final def post(scalaObjToPost: TO_SERVER): Unit = {
    val jsonStrToSend = jsonWriter(scalaObjToPost)
    jQuery.ajax(ajaxSettings(jsonStrToSend)) // perform the POST
  }

  private def ajaxSettings(jsonData: String): JQueryAjaxSettings = {
    val settingsObj = js.Dynamic.literal(
      url = postRelativeUrl,
      contentType = "application/json",
      data = jsonData,
      `type` = "POST",
      success = handleAjaxSuccess _,
      error = handleAjaxError _)

    settingsObj.asInstanceOf[org.scalajs.jquery.JQueryAjaxSettings]
  }

  private def handleAjaxError(jqXHR: JQueryXHR, textStatus: String, errorThrow: String) {
    onError(textStatus, errorThrow)
  }

  private def handleAjaxSuccess(data: js.Any, textStatus: String, jqXHR: JQueryXHR) {
    val jsonFromServer = jqXHR.responseText
    jsonReader(jsonFromServer) map { responseData: FROM_SERVER =>
      onSuccess(responseData, textStatus)
    } getOrElse {
      println(s"Failed to de-serialize JSON: $jsonFromServer")
    }
  }

}

