package eu.timepit.refined

import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class NumericSpec extends Properties("numeric") {
  property("Less") = forAll { (x: Int) =>
    implicitly[Predicate[Less[_5], Int]].isValid(x) == (x < 5)
  }

  property("LessEqual") = forAll { (x: Int) =>
    implicitly[Predicate[LessEqual[_5], Int]].isValid(x) == (x <= 5)
  }

  property("Greater") = forAll { (x: Int) =>
    implicitly[Predicate[Greater[_5], Int]].isValid(x) == (x > 5)
  }

  property("GreaterEqual") = forAll { (x: Int) =>
    implicitly[Predicate[GreaterEqual[_5], Int]].isValid(x) == (x >= 5)
  }

  property("ZeroToOne") = forAll { (x: Double) =>
    implicitly[Predicate[ZeroToOne, Double]].isValid(x) == (x >= 0.0 && x <= 1.0)
  }
}
