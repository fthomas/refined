package eu.timepit.refined

import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.string.LowerCase
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.test.illTyped

class RefinedSpec extends Properties("refined") {
  property("refine success") = secure {
    refine[Greater[_5], Int](6).isRight
  }

  property("refine failure") = secure {
    refine[LowerCase, String]("Hallo").isLeft
  }

  property("refineLit success with String") = secure {
    def ignore = refineLit[LowerCase, String]("hello")
    true
  }

  property("refineLit failure with String") = secure {
    illTyped("""refineLit[UpperCase, String]("hello")""")
    true
  }

  property("refineLit success") = secure {
    def ignore = refineLit[Greater[_10], Int](15)
    true
  }

  property("refineLit failure") = secure {
    illTyped("""refineLit[Greater[_10], Int](5)""")
    true
  }
}
