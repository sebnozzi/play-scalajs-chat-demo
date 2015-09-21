package example

import scala.scalajs.js
import example.chat.ChatExample

object ScalaJSExample extends js.JSApp {


  def main(): Unit = {
    ChatExample.doIt()    
  }


}

