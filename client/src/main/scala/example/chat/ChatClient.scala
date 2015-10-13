package example.chat

import org.scalajs.dom.WebSocket
import org.scalajs.dom.raw.Event
import org.scalajs.dom.raw.MessageEvent
import org.scalajs.dom.window
import upickle.default._
import scala.scalajs.js.Any.fromFunction1
import scala.scalajs.js.Any.fromString


class ChatClient(onMessage: (shared.ChatMsg) => Any) {
  private var optSocket: Option[WebSocket] = None

  connectWebSocket()

  def sendMsg(msg: shared.ChatMsg): Unit = {
    for (socket <- optSocket) {
      val json = write(shared.MessageTypedEvent(msg))
      socket.send(json)
    }
  }

  def connectWebSocket(): Unit = {

    val host = window.location.hostname
    val port = {
      val p = window.location.port
      if (p.isEmpty)
        ""
      else ":" + p
    }
    val wsProtocol = {
      val p = window.location.protocol
      if (p.startsWith("https"))
        "wss"
      else
        "ws"
    }

    val socket = new WebSocket(s"$wsProtocol://$host$port/socket")

    socket.onopen = { (e: Event) =>
      println("Connected to server via WebSocket")
      optSocket = Some(socket)
    }
    
    socket.onmessage = { (e: MessageEvent) =>
      val rawJsonStr = e.data.toString()
      val showMsgCmd = read[shared.ShowMessageCmd](rawJsonStr)
      onMessage(showMsgCmd.msg)
    }

  }

}
