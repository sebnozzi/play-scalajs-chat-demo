package com.sebnozzi.scalajs.example.chatclient.ui.jquery

import com.sebnozzi.scalajs.sanitizer._
import com.sebnozzi.scalajs.example.chatclient.ui.ChatUI

import org.scalajs.dom.document
import org.scalajs.jquery._

import scala.scalajs.js
import scala.scalajs.js.Any.fromBoolean


/**
 * Manages all UI aspects, like reacting to
 * UI events, interacting with the form, etc.
 * */
class JQueryChatUI(messageTypedCallback: (String) => Any) extends ChatUI {

  init()

  override def addChatMsg(msg: shared.ChatMsg): Unit = {
    val unsanitizedTxt = msg.txt
    val sanitizedTxt = sanitizeHTML(unsanitizedTxt)
    jQuery("#messages").prepend( s"""<li><div class="well">${sanitizedTxt}</div></li>""")
  }

  override def isPostSendingSelected: Boolean = {
    val selectedValue = jQuery("input[name='sendMethod'][type='radio']:checked").value().toString()
    selectedValue == "post"
  }


  private def init():Unit = {
    preventFormSubmit()

    jQuery(document).ready(() => {
      resetInput()
      initSendingMethod()
    })
  }

  private def preventFormSubmit(): Unit =
  jQuery("#sendForm").submit(onFormSubmit _)

  private def initSendingMethod() = {
    jQuery("#sendViaPost").attr("checked", "checked")
  }

  private def resetInput(): Unit = {
    jQuery("#msg").value("")
    jQuery("#msg").focus()
  }

  private def formInput(): String =
    jQuery("#msg").value().toString()

  private def onFormSubmit(ev: JQueryEventObject): js.Any = {
    val msg = formInput()
    messageTypedCallback.apply(msg)
    resetInput()
    false // don't actually submit the form
  }



}