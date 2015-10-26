package com.sebnozzi.scalajs.example.chatclient.ui.angular

import com.greencatsoft.angularjs.core.Scope

import scala.scalajs.js


trait ChatScope extends Scope {
  var messages: js.Array[String] = js.native
  var typedMessage: String = js.native
  var sendMethod: String = js.native
}