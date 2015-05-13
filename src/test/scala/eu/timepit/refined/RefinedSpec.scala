package eu.timepit.refined

import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.string.{ UpperCase, LowerCase }
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.tag.@@

class RefinedSpec extends Properties("refined") {
  property("refine success") = secure {
    refine[Greater[_5], Int](6).isRight
  }

  property("refine failure") = secure {
    refine[LowerCase, String]("Hallo").isLeft
  }

  property("macro") = secure {
    def ignore = refineLit[LowerCase, String]("hello")
    true
  }

  property("macro") = secure {
    def ignore = refineLit[UpperCase, String]("hello")
    true
  }
}
