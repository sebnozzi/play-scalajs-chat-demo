package example

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.WebSocket
import shared.SharedMessages
import org.scalajs.jquery._
import org.scalajs.dom.raw.Event
import org.scalajs.dom.raw.MessageEvent
import org.scalajs.dom.raw.ErrorEvent

object ScalaJSExample extends js.JSApp {

  var optSocket: Option[WebSocket] = None

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

    optSocket = connectWebSocket()
  }

  def addMessageToUI(msg: String): Unit = {
    jQuery("#messages").prepend(s"<li>$msg</li>")
  }

  def sendMsg(msg: String): Unit =
    for (socket <- optSocket) {
      socket.send(msg)
    }

  //  def postMsg(msg: String): Unit = {
  //    val ajaxSettings = js.Dynamic.literal(
  //      url = "/sendMsg",
  //      contentType = "text/plain",
  //      data = msg,
  //      `type` = "POST",
  //      success = { (data: js.Any, textStatus: String, jqXHR: JQueryXHR) =>
  //        println(s"Server says: $data")
  //        addMessageToUI(msg) // we may add our own message
  //      },
  //      error = { (jqXHR: JQueryXHR, textStatus: String, errorThrow: String) =>
  //        println("Error sending")
  //      }).asInstanceOf[org.scalajs.jquery.JQueryAjaxSettings]
  //    jQuery.ajax(ajaxSettings)
  //  }

  def connectWebSocket(): Option[WebSocket] = {
    val socket = new WebSocket("ws://localhost:9000/socket")

    socket.onopen = { (e: Event) =>
      println("Connected to server via WebSocket")
    }

    socket.onclose = { (e: Event) =>
      println("The server closed our WebSocket")
    }

    socket.onerror = { (e: ErrorEvent) =>
      println("Error with the WebSocket")
    }

    socket.onmessage = { (e: MessageEvent) =>
      addMessageToUI(e.data.toString())
    }

    Some(socket)
  }

}

object AjaxPost {

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
