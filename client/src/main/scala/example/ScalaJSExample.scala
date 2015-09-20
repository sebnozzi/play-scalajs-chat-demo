package example

import scala.scalajs.js
import org.scalajs.dom
import shared.SharedMessages

import org.scalajs.jquery._

object ScalaJSExample extends js.JSApp {

  def main(): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks

    def onFormSubmit(ev: JQueryEventObject):js.Any = {
      val msg = jQuery("#msg").value()
      println(s"This should be sent to server: $msg")      
      jQuery("#msg").value("")
      jQuery("#msg").focus()
      false // don't actually submit the form
    }
    
    jQuery("#sendForm").submit(onFormSubmit _)
    jQuery("#msg").focus()
  }

}
