package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.auto._
import eu.timepit.refined.util.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.test.illTyped

class StringUtilSpec extends Properties("util.string") {

  property("uri success") = secure {
    uri("file:///dev/null") ?= new java.net.URI("file:///dev/null")
  }

  property("uri failure") = wellTyped {
    illTyped("""uri("file:// /dev/null")""", "Uri predicate failed.*")
  }

  property("uuid success") = secure {
    uuid("9ecce884-47fe-4ba4-a1bb-1a3d71ed6530") ?=
      java.util.UUID.fromString("9ecce884-47fe-4ba4-a1bb-1a3d71ed6530")
  }

  property("uuid failure") = wellTyped {
    illTyped("""uuid("whops")""", "Uuid predicate failed.*")
  }
}
