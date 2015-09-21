package example.chat

import org.scalajs.dom.WebSocket
import shared.SharedMessages
import org.scalajs.jquery._
import org.scalajs.dom.raw.Event
import org.scalajs.dom.raw.MessageEvent
import org.scalajs.dom.raw.ErrorEvent
import upickle.default._
import scala.scalajs.js.Any.fromFunction1
import scala.scalajs.js.Any.fromString

class ChatClient(onMessage: (SharedMessages.ChatMsg) => Any) {
  private var optSocket: Option[WebSocket] = None

  connectWebSocket()

  def sendMsg(msg: SharedMessages.ChatMsg): Unit = {
    for (socket <- optSocket) {
      val json = write(msg)
      socket.send(json)
    }
  }

  def connectWebSocket(): Unit = {
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
      val rawJsonStr = e.data.toString()
      try {
        val chatMsg = read[SharedMessages.ChatMsg](rawJsonStr)
        onMessage(chatMsg)
      } catch {
        case e: upickle.Invalid =>
          println(s"Invalid JSON: $rawJsonStr")
      }
    }
  }

}
