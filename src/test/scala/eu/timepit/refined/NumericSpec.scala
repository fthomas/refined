package eu.timepit.refined

import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class NumericSpec extends Properties("numeric") {
  property("Less") = forAll { (i: Int) =>
    Predicate[Less[_5], Int].isValid(i) == (i < 5)
  }

  property("LessEqual") = forAll { (i: Int) =>
    Predicate[LessEqual[_5], Int].isValid(i) == (i <= 5)
  }

  property("Greater") = forAll { (i: Int) =>
    Predicate[Greater[_5], Int].isValid(i) == (i > 5)
  }

  property("GreaterEqual") = forAll { (i: Int) =>
    Predicate[GreaterEqual[_5], Int].isValid(i) == (i >= 5)
  }

  property("ZeroToOne") = forAll { (d: Double) =>
    Predicate[ZeroToOne, Double].isValid(d) == (d >= 0.0 && d <= 1.0)
  }
}
