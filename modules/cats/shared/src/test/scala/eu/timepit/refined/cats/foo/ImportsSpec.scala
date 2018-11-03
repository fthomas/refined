package eu.timepit.refined.cats.foo

import _root_.cats.implicits._
import eu.timepit.refined.cats._
import eu.timepit.refined.types.string.NonEmptyString
import org.scalacheck.Prop._
import org.scalacheck.Properties

class ImportsSpec extends Properties("imports") {

  property("NonEmptyString.show") = secure {
    NonEmptyString.unsafeFrom("abc").show ?= "abc"
  }
}
