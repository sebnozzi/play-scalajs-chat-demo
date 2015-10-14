package com.sebnozzi.scalajs.comm

import scala.util.Try

/**
 * Created by sebnozzi on 14/10/15.
 */
trait JsonSerialization[TO_SERVER, FROM_SERVER] {

  protected val jsonWriter: (TO_SERVER) => String
  protected val jsonReader: (String) => Try[FROM_SERVER]

}
