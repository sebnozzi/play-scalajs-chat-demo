package com.sebnozzi.scalajs.example.chatclient.ui

import com.sebnozzi.scalajs.example.chatclient.ui.angular.AngularChatUI
import com.sebnozzi.scalajs.example.chatclient.ui.jquery.JQueryChatUI
import org.scalajs.dom

/**
 * Created by sebnozzi on 26/10/15.
 */
object ChatUIFactory {

  def buildInstance(messageTypedCallback: (String) => Any): ChatUI = {
    if (usingAngular) {
      println("Using AngularJS")
      new AngularChatUI(messageTypedCallback = messageTypedCallback)
    } else {
      println("Using jQuery")
      new JQueryChatUI(messageTypedCallback = messageTypedCallback)
    }
  }

  def usingAngular:Boolean = {
    val htmlTags = dom.document.getElementsByTagName("html")
    val htmlTag = htmlTags(0)
    val ngAppAttribute = htmlTag.attributes.getNamedItem("ng-app")
    ngAppAttribute != null
  }

}
