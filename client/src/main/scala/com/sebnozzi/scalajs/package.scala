package com.sebnozzi.scalajs

import scala.scalajs.js


/**
 * Declares a Scala package of functions or symbols
 * expected to be found in the JavaScript global
 * namespace.
 *
 * In this case we are providing Scala type-safe
 * definition for one sanitizer-HTML function.
 **/

package object sanitizer extends js.GlobalScope {

  def sanitizeHTML(str: String): String = js.native

}
