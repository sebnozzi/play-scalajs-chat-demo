package com.sebnozzi.scalajs.example.chatclient.ui

import org.scalajs.dom.document
import org.scalajs.jquery._

import scala.scalajs.js
import scala.scalajs.js.Any.fromBoolean
import com.sebnozzi.scalajs.sanitizer._

/**
 *
 * */
class ChatUI(onUserInput: (String) => Any) {

  preventFormSubmit()

  jQuery(document).ready(() => {
    resetInput()
  })

  def preventFormSubmit(): Unit =
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

  def addChatMsg(msg: shared.ChatMsg): Unit = {
    val unsanitizedTxt = msg.txt
    val sanitizedTxt = sanitizeHTML(unsanitizedTxt)
    jQuery("#messages").prepend( s"""<div class="well">${sanitizedTxt}</div>""")
  }

  def isPostSendingSelected: Boolean = {
    val selectedValue = jQuery("input[name='sendMethod'][type='radio']:checked").value().toString()
    selectedValue == "post"
  }

}