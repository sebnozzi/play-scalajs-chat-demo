package example.chat

import scala.scalajs.js
import org.scalajs.jquery._
import org.scalajs.dom.document
import scala.scalajs.js.Any.fromBoolean


class ChatUI(onUserInput: (String) => Any) {

  prevetFormSubmit()

  jQuery(document).ready(() => {
    resetInput()
  })

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

  def showMsg(msg: shared.ChatMsg): Unit =
    jQuery("#messages").prepend(s"""<div class="well">${msg.txt}</div>""")

  def isPostSendingSelected:Boolean = {
    val selectedValue = jQuery("input[name='sendMethod'][type='radio']:checked").value().toString()
    selectedValue == "post"
  }
    
}