package com.sebnozzi.scalajs.example.chatclient.ui

import com.sebnozzi.scalajs.sanitizer._
import org.scalajs.jquery._

/**
 * Created by sebnozzi on 26/10/15.
 */
trait ChatUI {

  def addChatMsg(msg: shared.ChatMsg): Unit

  def isPostSendingSelected: Boolean

}
