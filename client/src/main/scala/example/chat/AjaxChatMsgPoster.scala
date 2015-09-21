package example.chat

import scala.scalajs.js
import org.scalajs.jquery._
import shared.SharedMessages
import upickle.default._
import scala.scalajs.js.Any.fromFunction3
import scala.scalajs.js.Any.fromString



object AjaxChatMsgPoster {

  def postMsg(chatMsg: SharedMessages.ChatMsg): Unit = {
    val jsonStr = write(chatMsg)
    val ajaxSettings = js.Dynamic.literal(
      url = "/postMsg",
      contentType = "application/json", // NOTE: should be JSON
      data = jsonStr,
      `type` = "POST",
      success = { (data: js.Any, textStatus: String, jqXHR: JQueryXHR) =>
        println(s"Server says: $data")
      },
      error = { (jqXHR: JQueryXHR, textStatus: String, errorThrow: String) =>
        println("Error sending")
      }).asInstanceOf[org.scalajs.jquery.JQueryAjaxSettings]
    jQuery.ajax(ajaxSettings)
  }

}
