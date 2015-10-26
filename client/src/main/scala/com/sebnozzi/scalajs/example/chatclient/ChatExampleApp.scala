package com.sebnozzi.scalajs.example.chatclient

import com.greencatsoft.angularjs.Angular
import com.sebnozzi.scalajs.example.chatclient.communication.angular.{AngularChatClientFactory, AngularMsgPostServiceFactory}
import com.sebnozzi.scalajs.example.chatclient.communication.jquery.ChatMsgPoster
import com.sebnozzi.scalajs.example.chatclient.communication.{ChatClient}
import com.sebnozzi.scalajs.example.chatclient.ui
import com.sebnozzi.scalajs.example.chatclient.ui.angular.ChatCtrl
import com.sebnozzi.scalajs.example.chatclient.ui.jquery.JQueryChatUI
import org.scalajs.dom

import scala.scalajs.js

/**
 * Simple chat-application example.
 *
 * This is the client part, implemented in Scala.js
 *
 * It illustrates sending and receiving serialized
 * Scala-objects (to/from JSON) to and from the server.
 *
 * It communicates either via web-sockets or AJAX
 * (POST) requests.
 *
 */
object ChatExampleApp extends js.JSApp {

  def main(): Unit = {
    if (isUsingAngular) {
      initAngular()
    } else {
      initJQuery()
    }
  }

  def isUsingAngular: Boolean = {
    val htmlTag = dom.document.getElementsByTagName("html")(0)
    htmlTag.attributes.getNamedItem("ng-app") != null
  }

  def initAngular(): Unit = {
    val module = Angular.module("chat-demo")

    module.factory[AngularMsgPostServiceFactory]
    module.factory[AngularChatClientFactory]

    module.controller(ChatCtrl)
  }

  def initJQuery(): Unit = {
    class JQueryApp {
      private val client = new ChatClient(onMessage = addMsgToUI)
      private val ui = new JQueryChatUI(messageTypedCallback = sendMsgToServer)

      client.connect()

      def addMsgToUI(msg: shared.ChatMsg): Unit = {
        ui.addChatMsg(msg)
      }

      def sendMsgToServer(userText: String): Unit = {
        val chatMsg = shared.ChatMsg(userText)

        if (ui.isPostSendingSelected)
          ChatMsgPoster.postMsg(chatMsg)
        else
          client.sendMsg(chatMsg)
      }
    }
    new JQueryApp()
  }

}
