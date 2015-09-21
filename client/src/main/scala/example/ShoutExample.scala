package example

import org.scalajs.dom
import shared.SharedMessages

object ShoutExample {

  def doIt(): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
  }

}