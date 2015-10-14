package com.sebnozzi.scalajs.example.chat

import com.sebnozzi.scalajs.comm.AjaxPoster
import upickle.default._

import scala.util.Try

/**
 * Created by sebnozzi on 14/10/15.
 */
object AjaxChatMsgPoster extends AjaxPoster[shared.MessageTypedEvent, shared.MessageReceived] {

  override val postRelativeUrl = "/postMsg"

  override val jsonWriter = (o: shared.MessageTypedEvent) => write(o)
  override val jsonReader = (s: String) => Try(read[shared.MessageReceived](s))

  def postMsg(msg: shared.ChatMsg): Unit = {
    post(shared.MessageTypedEvent(msg))
  }

  override protected def onSuccess(responseData: shared.MessageReceived, textStatus: String): Unit = {
    responseData match {
      case shared.MessageReceived() =>
        println("POST succeeded")
    }
  }

  override protected def onError(textStatus: String, errorThrow: String): Unit =
    println(s"Could not POST - something went wrong: $errorThrow")

}
