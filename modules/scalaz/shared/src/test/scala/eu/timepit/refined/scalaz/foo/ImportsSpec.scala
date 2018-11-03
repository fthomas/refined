package eu.timepit.refined.scalaz.foo

import eu.timepit.refined.scalaz._
import eu.timepit.refined.types.string.NonEmptyString
import org.scalacheck.Prop._
import org.scalacheck.Properties
import scalaz.Scalaz._

class ImportsSpec extends Properties("imports") {

  property("NonEmptyString.show") = secure {
    NonEmptyString.unsafeFrom("abc").shows ?= "\"abc\""
  }
}
