package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.numeric._
import org.scalacheck.{Arbitrary, Gen, Properties}
import org.scalacheck.Prop._
import shapeless.nat._

class NumericValidateSpec extends Properties("NumericValidate") {

  property("Less.isValid") = forAll((d: Double) => isValid[Less[W.`1.0`.T]](d) ?= (d < 1.0))

  property("Less.showExpr") = secure {
    showExpr[Less[W.`1.1`.T]](0.1) ?= "(0.1 < 1.1)"
  }

  property("Less.Nat.isValid") = forAll((i: Int) => isValid[Less[_5]](i) ?= (i < 5))

  property("Less.Nat.showExpr") = secure {
    showExpr[Less[_5]](0) ?= "(0 < 5)"
  }

  property("LessEqual.Nat.isValid") = forAll((i: Int) => isValid[LessEqual[_5]](i) ?= (i <= 5))

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

  property("Greater.Nat.isValid") = forAll((i: Int) => isValid[Greater[_5]](i) ?= (i > 5))

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

  property("Modulo.isValid - Nat - Byte") = forAll { (b: Byte) =>
    isValid[Modulo[_2, _0]](b) ?= (b % 2 == 0)
  }

  property("Modulo.isValid - Nat - Short") = forAll { (s: Short) =>
    isValid[Modulo[_2, _0]](s) ?= (s % 2 == 0)
  }

  property("Modulo.isValid - Nat - Int") = forAll { (i: Int) =>
    isValid[Modulo[_2, _0]](i) ?= (i % 2 == 0)
  }

  property("Modulo.isValid - Wit - Int") = forAll { (i: Int) =>
    isValid[Modulo[W.`2`.T, W.`0`.T]](i) ?= (i % 2 == 0)
  }

  property("Modulo.isValid - Nat - Long") = forAll { (l: Long) =>
    isValid[Modulo[_2, _0]](l) ?= (l % 2 == 0)
  }

  property("Modulo.isValid - Wit - Long") = forAll { (l: Long) =>
    isValid[Modulo[W.`2L`.T, W.`0L`.T]](l) ?= (l % 2 == 0)
  }

  property("Modulo.showExpr") = secure {
    showExpr[Modulo[W.`2`.T, W.`0`.T]](4) ?= s"(${4} % ${2} == ${0})"
  }

  property("Modulo.Nat.isValid") = forAll((i: Int) => isValid[Modulo[_2, _0]](i) ?= (i % 2 == 0))

  property("Modulo.Nat.showExpr") = secure {
    showExpr[Modulo[_2, _0]](4) ?= "(4 % 2 == 0)"
  }

  property("Modulo.Nat ~= Modulo.Int") = forAll { (i: Int) =>
    showResult[Modulo[_5, _2]](i) ?= showResult[Modulo[W.`5`.T, W.`2`.T]](i)
  }

  property("Divisible.Nat.isValid") = forAll { (i: Int) =>
    isValid[Divisible[_2]](i) ?= (i % 2 == 0)
  }

  property("Divisible.Int.isValid") = forAll { (i: Int) =>
    isValid[Divisible[W.`2`.T]](i) ?= (i % 2 == 0)
  }

  property("Divisible.Nat.showExpr") = secure {
    showExpr[Divisible[_2]](4) ?= "(4 % 2 == 0)"
  }

  property("Divisible.Int.showExpr") = secure {
    showExpr[Divisible[W.`2`.T]](4) ?= "(4 % 2 == 0)"
  }

  property("NonDivisible.isValid") = forAll { (i: Int) =>
    isValid[NonDivisible[_2]](i) ?= (i % 2 != 0)
  }

  property("NonDivisible.showExpr") = secure {
    showExpr[NonDivisible[_2]](4) ?= "!(4 % 2 == 0)"
  }

  property("Even.isValid") = forAll((i: Int) => isValid[Even](i) ?= (i % 2 == 0))

  property("Even.showExpr") = secure {
    showExpr[Even](4) ?= "(4 % 2 == 0)"
  }

  property("Odd.isValid") = forAll((i: Int) => isValid[Odd](i) ?= (i % 2 != 0))

  property("Odd.showExpr") = secure {
    showExpr[Odd](4) ?= "!(4 % 2 == 0)"
  }

  property("Interval.Open.isValid") = forAll { (d: Double) =>
    isValid[Interval.Open[_0, _1]](d) ?= (d > 0.0 && d < 1.0)
  }

  property("Interval.Open.showExpr") = secure {
    val s = showExpr[Interval.Open[_0, _1]](0.5)
    (s ?= "((0.5 > 0) && (0.5 < 1))") || (s ?= "((0.5 > 0.0) && (0.5 < 1.0))")
  }

  property("Interval.OpenClosed.isValid") = forAll { (d: Double) =>
    isValid[Interval.OpenClosed[_0, _1]](d) ?= (d > 0.0 && d <= 1.0)
  }

  property("Interval.OpenClosed.showExpr") = secure {
    val s = showExpr[Interval.OpenClosed[_0, _1]](0.5)
    (s ?= "((0.5 > 0) && !(0.5 > 1))") || (s ?= "((0.5 > 0.0) && !(0.5 > 1.0))")
  }

  property("Interval.ClosedOpen.isValid") = forAll { (d: Double) =>
    isValid[Interval.ClosedOpen[_0, _1]](d) ?= (d >= 0.0 && d < 1.0)
  }

  property("Interval.ClosedOpen.showExpr") = secure {
    val s = showExpr[Interval.ClosedOpen[_0, _1]](0.5)
    (s ?= "(!(0.5 < 0) && (0.5 < 1))") || (s ?= "(!(0.5 < 0.0) && (0.5 < 1.0))")
  }

  property("Interval.Closed.isValid") = forAll { (d: Double) =>
    isValid[Interval.Closed[_0, _1]](d) ?= (d >= 0.0 && d <= 1.0)
  }

  property("Interval.Closed.showExpr") = secure {
    val s = showExpr[Interval.Closed[_0, _1]](0.5)
    (s ?= "(!(0.5 < 0) && !(0.5 > 1))") || (s ?= "(!(0.5 < 0.0) && !(0.5 > 1.0))")
  }

  val floatWithNaN: Gen[Float] = Gen.frequency(8 -> Arbitrary.arbitrary[Float], 2 -> Float.NaN)
  val doubleWithNaN: Gen[Double] = Gen.frequency(8 -> Arbitrary.arbitrary[Double], 2 -> Double.NaN)

  property("NonNaN.Float.isValid") = forAll(floatWithNaN) { (d: Float) =>
    isValid[NonNaN](d) ?= !d.isNaN
  }

  property("NonNaN.Float.showExpr") = secure {
    showExpr[NonNaN](Float.NaN) ?= "(NaN != NaN)"
  }

  property("NonNaN.Double.isValid") = forAll(doubleWithNaN) { (d: Double) =>
    isValid[NonNaN](d) ?= !d.isNaN
  }

  property("NonNaN.Double.showExpr") = secure {
    showExpr[NonNaN](Double.NaN) ?= "(NaN != NaN)"
  }

}
