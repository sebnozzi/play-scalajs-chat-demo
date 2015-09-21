package example.chat

import scala.scalajs.js
import shared.SharedMessages
import org.scalajs.jquery._
import upickle.default._
import scala.scalajs.js.Any.fromBoolean
import scala.scalajs.js.Any.fromFunction1
import scala.scalajs.js.Any.fromString

class ChatUI(onUserInput: (String) => Any) {

  prevetFormSubmit()
  resetInput()

  def prevetFormSubmit(): Unit =
    jQuery("#sendForm").submit(onFormSubmit _)

  def resetInput(): Unit = {
    jQuery("#msg").value("")
    jQuery("#msg").focus()
  }

  def formInput(): String =
    jQuery("#msg").value().toString()

  def onFormSubmit(ev: JQueryEventObject): js.Any = {
    val msg = formInput()
    onUserInput(msg)
    resetInput()
    false // don't actually submit the form
  }

  def showMsg(msg: SharedMessages.ChatMsg): Unit =
    jQuery("#messages").prepend(s"<li>${msg.txt}</li>")

}