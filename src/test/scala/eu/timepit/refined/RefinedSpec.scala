package eu.timepit.refined

import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.string.LowerCase
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class RefinedSpec extends Properties("refined") {
  property("refine success") = secure {
    refine[Greater[_5], Int](6).isRight
  }

  property("refine failure") = secure {
    refine[LowerCase, String]("Hallo").isLeft
  }
}
