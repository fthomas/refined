package eu.timepit.refined

import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class NumericSpec extends Properties("numeric") {
  property("Less.isValid") = forAll { (i: Int) =>
    Predicate[Less[W.`5`.T], Int].isValid(i) ?= (i < 5)
  }

  property("Less.isValid.Nat") = forAll { (i: Int) =>
    Predicate[Less[_5], Int].isValid(i) ?= (i < 5)
  }

  property("Less.show") = secure {
    Predicate[Less[_5], Int].show(0) ?= "(0 < 5)"
  }

  property("LessEqual.isValid") = forAll { (i: Int) =>
    Predicate[LessEqual[_5], Int].isValid(i) ?= (i <= 5)
  }

  property("LessEqual.show") = secure {
    Predicate[LessEqual[_5], Int].show(0) ?= "!(0 > 5)"
  }

  property("Greater.isValid") = forAll { (i: Int) =>
    Predicate[Greater[W.`5`.T], Int].isValid(i) ?= (i > 5)
  }

  property("Greater.isValid.Nat") = forAll { (i: Int) =>
    Predicate[Greater[_5], Int].isValid(i) ?= (i > 5)
  }

  property("Greater.show") = secure {
    Predicate[Greater[_5], Int].show(0) ?= "(0 > 5)"
  }

  property("GreaterEqual.isValid") = forAll { (i: Int) =>
    Predicate[GreaterEqual[_5], Int].isValid(i) ?= (i >= 5)
  }

  property("GreaterEqual.show") = secure {
    Predicate[GreaterEqual[_5], Int].show(0) ?= "!(0 < 5)"
  }

  property("Interval[_0, _1].isValid") = forAll { (d: Double) =>
    Predicate[Interval[_0, _1], Double].isValid(d) ?= (d >= 0.0 && d <= 1.0)
  }

  property("Equal.isValid") = forAll { (i: Int) =>
    Predicate[Equal[_5], Int].isValid(i) ?= (i == 5)
  }

  property("Equal.show") = secure {
    Predicate[Equal[_5], Int].show(0) ?= "(0 == 5)"
  }
}
