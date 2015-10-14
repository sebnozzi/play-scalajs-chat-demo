package com.sebnozzi.scalajs.comm

import org.scalajs.dom._

/**
 * Created by sebnozzi on 14/10/15.
 */
object WebSocketUrlBuilder {
  private val host = window.location.hostname
  private val port = {
    val p = window.location.port
    if (p.isEmpty)
      ""
    else ":" + p
  }
  private val wsProtocol = {
    val p = window.location.protocol
    if (p.startsWith("https"))
      "wss"
    else
      "ws"
  }

  def fullUrl(relativeUrl: String): String = {
    s"$wsProtocol://$host$port$relativeUrl"
  }
}