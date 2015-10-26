package com.sebnozzi.scalajs.example.chatclient.communication.angular

import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core.HttpService
import com.sebnozzi.scalajs.example.chatclient.communication.ServerURLs
import upickle.default._

import scala.scalajs.js
import scala.scalajs.js.JSON

/**
 * Created by sebnozzi on 26/10/15.
 */
@injectable("msgPostSvc")
class AngularMsgPostService(http: HttpService) extends Service {

  def postMessage(msg: shared.ChatMsg): Unit = {
    val jsonData = write(shared.MessageTypedEvent(msg))
    http.post[js.Any](ServerURLs.postMsgURL, jsonData).success(onSuccess _).error(onError _)
  }

  private def onSuccess(data: js.Any): Unit = {
    val dataStr = JSON.stringify(data)
    val response = read[shared.MessageReceived](dataStr)
    println(s"Server says: $response")
  }

  private def onError(data: js.Any, status: Int): Unit = {
    println(s"Server responded with status: $status")
  }

}

@injectable("msgPostSvc")
class AngularMsgPostServiceFactory(http: HttpService) extends Factory[AngularMsgPostService] {

  override def apply(): AngularMsgPostService = new AngularMsgPostService(http)
}