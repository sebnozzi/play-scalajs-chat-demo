package example

import scala.scalajs.js
import org.scalajs.jquery._


object AjaxPostExample {

  def post(data: js.Any): Unit = {
    val ajaxSettings = js.Dynamic.literal(
      url = "/sendMsg",
      contentType = "text/plain",
      data = data,
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
