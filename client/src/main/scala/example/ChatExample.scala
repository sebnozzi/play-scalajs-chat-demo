package example

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.WebSocket
import shared.SharedMessages
import shared.SharedMessages.ChatMsg

import org.scalajs.jquery._
import org.scalajs.dom.raw.Event
import org.scalajs.dom.raw.MessageEvent
import org.scalajs.dom.raw.ErrorEvent
import upickle.default._

object ChatExample {
  
  private var optSocket: Option[WebSocket] = None
  
  def doIt(): Unit = {

    def onFormSubmit(ev: JQueryEventObject): js.Any = {
      val msg = jQuery("#msg").value().toString()
      sendMsg(msg)
      jQuery("#msg").value("")
      jQuery("#msg").focus()
      false // don't actually submit the form
    }

    jQuery("#sendForm").submit(onFormSubmit _)
    jQuery("#msg").focus()

    connectWebSocket()
  }

  def addMessageToUI(msg: String): Unit = {
    jQuery("#messages").prepend(s"<li>$msg</li>")
  }

  def sendMsg(textMsg: String): Unit =
    for (socket <- optSocket) {
      val json = write(ChatMsg(textMsg))
      socket.send(json)
    }

  def connectWebSocket():Unit = {
    val socket = new WebSocket("ws://localhost:9000/socket")

    socket.onopen = { (e: Event) =>
      println("Connected to server via WebSocket")
      optSocket = Some(socket)
    }

    socket.onclose = { (e: Event) =>
      println("The server closed our WebSocket")
      optSocket = None
    }

    socket.onerror = { (e: ErrorEvent) =>
      println("Error with the WebSocket")
    }

    socket.onmessage = { (e: MessageEvent) =>
      addMessageToUI(e.data.toString())
    }
  }
  
  
}