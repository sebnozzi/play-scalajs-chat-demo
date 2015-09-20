package example

import scala.scalajs.js
import org.scalajs.dom
import shared.SharedMessages

import org.scalajs.jquery._

object ScalaJSExample extends js.JSApp {

  def main(): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks

    def onFormSubmit(ev: JQueryEventObject): js.Any = {
      val msg = jQuery("#msg").value().toString()
      sendMsg(msg)
      jQuery("#msg").value("")
      jQuery("#msg").focus()
      false // don't actually submit the form
    }

    jQuery("#sendForm").submit(onFormSubmit _)
    jQuery("#msg").focus()
  }

  def sendMsg(msg: String): Unit = {
    val ajaxSettings = js.Dynamic.literal(
      url = "/sendMsg",
      contentType = "text/plain",
      data = msg,
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
