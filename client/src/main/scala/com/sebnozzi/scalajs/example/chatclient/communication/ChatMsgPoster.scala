package com.sebnozzi.scalajs.example.chatclient.communication

import com.sebnozzi.scalajs.communication.ajax.AjaxPoster
import com.sebnozzi.scalajs.communication.serialization.JsonSerialization
import upickle.default._

import scala.util.Try

/**
 * Sends AJAX-POST requests about chat-messages to the chat-server.
 */
object ChatMsgPoster {

  private object MyMsgPoster
    extends AjaxPoster[shared.MessageTypedEvent, shared.MessageReceived]
    with ChatMsgPosterSerialization {

    override val postRelativeUrl = "/postMsg"

    override protected def onSuccess(responseData: shared.MessageReceived, textStatus: String): Unit = {
      println("POST succeeded")
    }

    override protected def onError(textStatus: String, errorThrow: String): Unit =
      println(s"Could not POST - something went wrong: $errorThrow")

  }

  def postMsg(msg: shared.ChatMsg): Unit = {
    MyMsgPoster.post(shared.MessageTypedEvent(msg))
  }

}

trait ChatMsgPosterSerialization extends JsonSerialization[shared.MessageTypedEvent, shared.MessageReceived] {

  override val jsonWriter =
    (o: shared.MessageTypedEvent) => write(o)

  override val jsonReader =
    (s: String) => Try(read[shared.MessageReceived](s))

}