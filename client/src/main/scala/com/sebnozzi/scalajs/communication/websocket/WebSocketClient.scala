package com.sebnozzi.scalajs.communication.websocket

import com.sebnozzi.scalajs.communication.serialization.JsonSerialization
import org.scalajs.dom.raw._

/**
 * Handles a web-socket lifecycle.
 *
 * Connects to a specific end-point in the server.
 * Sends JSON-serialized Scala-objects to the server,
 * and processes incoming JSON-serialized Scala-objects from the server.
 *
 * After creation YOU have to call connect() yourself.
 *
 */
abstract class WebSocketClient[TO_SERVER, FROM_SERVER] extends JsonSerialization[TO_SERVER, FROM_SERVER] {

  private var optSocket: Option[WebSocket] = None

  /**
   * Override to specify the relative-URL of the end-point in the server
   * to connect to. ONLY relative-URLs are allowed!
   **/
  def relativeSocketUrl: String

  /**
   * Override to handle incoming Scala-objects from the server.
   **/
  def receive(incomingObj: FROM_SERVER): Unit

  /**
   * Call this method after creating an instance of this class.
   **/
  final def connect(): Unit = {
    val socket = new WebSocket(WebSocketUrlBuilder.fullUrl(relativeSocketUrl))
    socket.onopen = handleSocketConnected(socket, _: Event)
    socket.onmessage = handleIncomingMessage(_: MessageEvent)
    socket.onclose = handleSocketClosed(_: CloseEvent)
    socket.onerror = handleSocketError(_: ErrorEvent)
  }

  /**
   * Call to send Scala-objects to the server.
   * The response, if any, will be sent asynchronously and handled
   * by the method "receive".
   **/
  final def send(data: TO_SERVER): Unit = {
    for (socket <- optSocket) {
      val json = jsonWriter(data)
      socket.send(json)
    }
  }

  private def handleSocketConnected(connectedSocket: WebSocket, e: Event): Unit = {
    println("Connected to server via WebSocket")
    optSocket = Some(connectedSocket)
  }

  private def handleIncomingMessage(e: MessageEvent): Unit = {
    val rawJsonStr = e.data.toString()
    val tryScalaObject = jsonReader(rawJsonStr)
    tryScalaObject map {
      receive(_)
    } getOrElse {
      println(s"Failed to de-serialize JSON: $rawJsonStr")
    }
  }

  private def handleSocketClosed(closedEvent: CloseEvent) = {
    println(s"WebSocket closed by the server: ${closedEvent.reason}")
    optSocket = None
  }

  private def handleSocketError(event: ErrorEvent) = {
    println(s"WebSocket had an error: ${event.message}")
  }

}

