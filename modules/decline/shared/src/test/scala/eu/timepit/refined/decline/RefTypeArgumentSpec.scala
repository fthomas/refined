package eu.timepit.refined.decline

import _root_.com.monovore.decline._
import cats.data.Validated
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties

class RefTypeArgumentSpec extends Properties("RefTypeArgument") {

  type PosInt = Int Refined Positive

  property("successful argument assignment") = secure {
    Argument[PosInt].read("1") ?=
      Validated.validNel(1)
  }

  property("invalid argument assignment (predicate)") = secure {
    Argument[PosInt].read("0") ?=
      Validated.invalidNel("Predicate failed: (0 > 0).")
  }

  property("invalid argument assignment (wrong type)") = secure {
    Argument[PosInt].read("abc") ?=
      Validated.invalidNel("Invalid integer: abc")
  }

}
