package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class NumericValidateSpec extends Properties("NumericValidate") {

  property("Less.isValid") = forAll { (d: Double) =>
    isValid[Less[W.`1.0`.T]](d) ?= (d < 1.0)
  }

  property("Less.showExpr") = secure {
    showExpr[Less[W.`1.1`.T]](0.1) ?= "(0.1 < 1.1)"
  }

  property("Less.Nat.isValid") = forAll { (i: Int) =>
    isValid[Less[_5]](i) ?= (i < 5)
  }

  property("Less.Nat.showExpr") = secure {
    showExpr[Less[_5]](0) ?= "(0 < 5)"
  }

  property("LessEqual.Nat.isValid") = forAll { (i: Int) =>
    isValid[LessEqual[_5]](i) ?= (i <= 5)
  }

  property("LessEqual.Nat.showExpr") = secure {
    showExpr[LessEqual[_5]](0) ?= "!(0 > 5)"
  }

  property("Less.Nat ~= Less.Int") = forAll { (i: Int) =>
    showResult[Less[_5]](i) ?= showResult[Less[W.`5`.T]](i)
  }

  property("Greater.isValid") = forAll { (d: Double) =>
    isValid[Greater[W.`1.0`.T]](d) ?= (d > 1.0)
  }

  property("Greater.showExpr") = secure {
    showExpr[Greater[W.`1.1`.T]](0.1) ?= "(0.1 > 1.1)"
  }

  property("Greater.Nat.isValid") = forAll { (i: Int) =>
    isValid[Greater[_5]](i) ?= (i > 5)
  }

  property("Greater.Nat.showExpr") = secure {
    showExpr[Greater[_5]](0) ?= "(0 > 5)"
  }

  property("GreaterEqual.Nat.isValid") = forAll { (i: Int) =>
    isValid[GreaterEqual[_5]](i) ?= (i >= 5)
  }

  property("GreaterEqual.Nat.showExpr") = secure {
    showExpr[GreaterEqual[_5]](0) ?= "!(0 < 5)"
  }

  property("Greater.Nat ~= Greater.Int") = forAll { (i: Int) =>
    showResult[Greater[_5]](i) ?= showResult[Greater[W.`5`.T]](i)
  }

  property("Interval.Open.isValid") = forAll { (d: Double) =>
    isValid[Interval.Open[_0, _1]](d) ?= (d > 0.0 && d < 1.0)
  }

  property("Interval.Open.showExpr") = secure {
    showExpr[Interval.Open[_0, _1]](0.5) ?= "((0.5 > 0) && (0.5 < 1))"
  }

  property("Interval.OpenClosed.isValid") = forAll { (d: Double) =>
    isValid[Interval.OpenClosed[_0, _1]](d) ?= (d > 0.0 && d <= 1.0)
  }

  property("Interval.OpenClosed.showExpr") = secure {
    showExpr[Interval.OpenClosed[_0, _1]](0.5) ?= "((0.5 > 0) && !(0.5 > 1))"
  }

  property("Interval.ClosedOpen.isValid") = forAll { (d: Double) =>
    isValid[Interval.ClosedOpen[_0, _1]](d) ?= (d >= 0.0 && d < 1.0)
  }

  property("Interval.ClosedOpen.showExpr") = secure {
    showExpr[Interval.ClosedOpen[_0, _1]](0.5) ?= "(!(0.5 < 0) && (0.5 < 1))"
  }

  property("Interval.Closed.isValid") = forAll { (d: Double) =>
    isValid[Interval.Closed[_0, _1]](d) ?= (d >= 0.0 && d <= 1.0)
  }

  property("Interval.Closed.showExpr") = secure {
    showExpr[Interval.Closed[_0, _1]](0.5) ?= "(!(0.5 < 0) && !(0.5 > 1))"
  }
}
