package shared

case class ChatMsg(txt: String)

// Message typed by one user on the UI
case class MessageTypedEvent(msg: ChatMsg)

// Message to be shown on the "messages" list
case class ShowMessageCmd(msg: ChatMsg)

