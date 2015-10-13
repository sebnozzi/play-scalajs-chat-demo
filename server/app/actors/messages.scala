package actors

case class LoginCmd()
case class LogoutCmd()
case class BroadcastCmd(msg: shared.ChatMsg)
