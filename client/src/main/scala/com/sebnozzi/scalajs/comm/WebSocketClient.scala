package com.sebnozzi.scalajs.comm

import org.scalajs.dom.raw.{Event, MessageEvent, WebSocket}

/**
 * Created by sebnozzi on 13/10/15.
 */
abstract class WebSocketClient[TO_SERVER, FROM_SERVER] extends JsonSerialization[TO_SERVER, FROM_SERVER] {

  private var optSocket: Option[WebSocket] = None

  def relativeSocketUrl: String

  def receive(incomingObj: FROM_SERVER): Unit

  connectWebSocket()

  def send(data: TO_SERVER): Unit = {
    for (socket <- optSocket) {
      val json = jsonWriter(data)
      socket.send(json)
    }
  }

  private def connectWebSocket(): Unit = {

    val socket = new WebSocket(WebSocketUrlBuilder.fullUrl(relativeSocketUrl))

    socket.onopen = { (e: Event) =>
      println("Connected to server via WebSocket")
      optSocket = Some(socket)
    }

    socket.onmessage = { (e: MessageEvent) =>
      val rawJsonStr = e.data.toString()
      val tryScalaObject = jsonReader(rawJsonStr)
      tryScalaObject map { scalaObj: FROM_SERVER =>
        receive(scalaObj)
      } getOrElse {
        println(s"Failed to de-serialize JSON: $rawJsonStr")
      }
    }
  }

}

