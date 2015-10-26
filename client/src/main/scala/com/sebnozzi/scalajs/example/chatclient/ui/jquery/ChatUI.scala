package com.sebnozzi.scalajs.example.chatclient.ui.jquery

import com.sebnozzi.scalajs.sanitizer._
import org.scalajs.dom.document
import org.scalajs.jquery._

import scala.scalajs.js
import scala.scalajs.js.Any.fromBoolean

/**
 * Manages all UI aspects, like reacting to
 * UI events, interacting with the form, etc.
 * */
class ChatUI(onUserInput: (String) => Any) {

  init()

  def addChatMsg(msg: shared.ChatMsg): Unit = {
    val unsanitizedTxt = msg.txt
    val sanitizedTxt = sanitizeHTML(unsanitizedTxt)
    jQuery("#messages").prepend( s"""<div class="well">${sanitizedTxt}</div>""")
  }

  def isPostSendingSelected: Boolean = {
    val selectedValue = jQuery("input[name='sendMethod'][type='radio']:checked").value().toString()
    selectedValue == "post"
  }

  private def init():Unit = {
    preventFormSubmit()

    jQuery(document).ready(() => {
      resetInput()
    })
  }

  private def preventFormSubmit(): Unit =
    jQuery("#sendForm").submit(onFormSubmit _)

  private def resetInput(): Unit = {
    jQuery("#msg").value("")
    jQuery("#msg").focus()
  }

  private def formInput(): String =
    jQuery("#msg").value().toString()

  private def onFormSubmit(ev: JQueryEventObject): js.Any = {
    val msg = formInput()
    onUserInput(msg)
    resetInput()
    false // don't actually submit the form
  }



}