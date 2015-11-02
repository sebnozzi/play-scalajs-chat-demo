package shared

case class ChatMsg(txt: String) {
  val isEmpty = txt.trim.isEmpty
}

abstract sealed class Protocol

// Message typed by one user on the UI
case class MessageTypedEvent(msg: ChatMsg) extends Protocol

// Message to be shown on the "messages" list
case class ShowMessageCmd(msg: ChatMsg) extends Protocol

// Sent by the server when a POST request succeeds
case class MessageReceived() extends Protocol
