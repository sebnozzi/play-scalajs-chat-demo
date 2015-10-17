package com.sebnozzi.scalajs.communication.serialization

import scala.util.Try

/**
 * Simple trait to fill-in lambdas that serialize from Scala-types
 * to JSON and back.
 */
trait JsonSerialization[TO_SERVER, FROM_SERVER] {

  /**
   * This anonymous function should provide a way to convert
   * a Scala-object to serialized JSON before sending to the server.
   * */
  protected val jsonWriter: (TO_SERVER) => String

  /**
   * This anonymous function should provide a way to convert
   * incoming serialized JSON coming from the server back
   * to a Scala-object.
   * */
  protected val jsonReader: (String) => Try[FROM_SERVER]

}
