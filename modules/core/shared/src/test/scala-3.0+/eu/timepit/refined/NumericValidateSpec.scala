package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.{Arbitrary, Gen, Properties}

class NumericValidateSpec extends Properties("NumericValidate") {

  property("isValid[Less[1]](d: Double)") = forAll { (d: Double) =>
    isValid[Less[1]](d) ?= (d < 1)
  }

  property("isValid[Less[1.0]](d: Double)") = forAll { (d: Double) =>
    isValid[Less[1.0]](d) ?= (d < 1.0)
  }

  property("isValid[Less[1.0]](d: BigDecimal)") = forAll { (d: BigDecimal) =>
    isValid[Less[1.0]](d) ?= (d < 1.0)
  }

  property("showExpr[Less[5]](0)") = secure {
    showExpr[Less[5]](0) ?= "(0 < 5)"
  }

  property("showExpr[Less[1.1]](0.1)") = secure {
    showExpr[Less[1.1]](0.1) ?= "(0.1 < 1.1)"
  }

  property("isValid[LessEqual[5]](i: Int)") = forAll { (i: Int) =>
    isValid[LessEqual[5]](i) ?= (i <= 5)
  }

  property("showExpr[LessEqual[5]](0)") = secure {
    showExpr[LessEqual[5]](0) ?= "!(0 > 5)"
  }

  property("isValid[Greater[1.0]](b: Byte)") = forAll { (b: Byte) =>
    isValid[Greater[1]](b) ?= (b > 1)
  }

  property("isValid[Greater[1.0]](d: Double)") = forAll { (d: Double) =>
    isValid[Greater[1.0]](d) ?= (d > 1.0)
  }

  property("showExpr[Greater[1.1]](0.1)") = secure {
    showExpr[Greater[1.1]](0.1) ?= "(0.1 > 1.1)"
  }

  property("isValid[GreaterEqual[5L]](l: Long)") = forAll { (l: Long) =>
    isValid[GreaterEqual[5L]](l) ?= (l >= 5L)
  }

  property("showExpr[GreaterEqual[5]](0)") = secure {
    showExpr[GreaterEqual[5]](0) ?= "!(0 < 5)"
  }

  property("isValid[Modulo[2, 0]](b: Byte)") = forAll { (b: Byte) =>
    isValid[Modulo[2, 0]](b) ?= (b % 2 == 0)
  }

  property("isValid[Modulo[2, 0]](s: Short)") = forAll { (s: Short) =>
    isValid[Modulo[2, 0]](s) ?= (s % 2 == 0)
  }

  property("isValid[Modulo[2, 0]](i: Int)") = forAll { (i: Int) =>
    isValid[Modulo[2, 0]](i) ?= (i % 2 == 0)
  }

  property("isValid[Modulo[2, 0]](l: Long)") = forAll { (l: Long) =>
    isValid[Modulo[2, 0]](l) ?= (l % 2 == 0)
  }

  property("isValid[Modulo[2L, 0L]](l: Long)") = forAll { (l: Long) =>
    isValid[Modulo[2L, 0L]](l) ?= (l % 2 == 0)
  }

  property("showExpr[Modulo[2, 0]](4)") = secure {
    showExpr[Modulo[2, 0]](4) ?= s"(${4} % ${2} == ${0})"
  }

  property("isValid[Divisible[2]](i: Int)") = forAll { (i: Int) =>
    isValid[Divisible[2]](i) ?= (i % 2 == 0)
  }

  property("showExpr[Divisible[2]](4)") = secure {
    showExpr[Divisible[2]](4) ?= "(4 % 2 == 0)"
  }

  property("isValid[NonDivisible[2]](i: Int)") = forAll { (i: Int) =>
    isValid[NonDivisible[2]](i) ?= (i % 2 != 0)
  }

  property("showExpr[NonDivisible[2]](4)") = secure {
    showExpr[NonDivisible[2]](4) ?= "!(4 % 2 == 0)"
  }

  property("isValid[Even](i: Int)") = forAll { (i: Int) =>
    isValid[Even](i) ?= (i % 2 == 0)
  }

  property("showExpr[Even](4)") = secure {
    showExpr[Even](4) ?= "(4 % 2 == 0)"
  }

  property("isValid[Odd](i: Int)") = forAll { (i: Int) =>
    isValid[Odd](i) ?= (i % 2 != 0)
  }

  property("showExpr[Odd](4)") = secure {
    showExpr[Odd](4) ?= "!(4 % 2 == 0)"
  }

  property("isValid[Interval.Open[0, 1]](d: Double)") = forAll { (d: Double) =>
    isValid[Interval.Open[0, 1]](d) ?= (d > 0.0 && d < 1.0)
  }

  property("showExpr[Interval.Open[0, 1]](0.5)") = secure {
    val s = showExpr[Interval.Open[0, 1]](0.5)
    (s ?= "((0.5 > 0) && (0.5 < 1))") || (s ?= "((0.5 > 0.0) && (0.5 < 1.0))")
  }

  property("isValid[Interval.OpenClosed[0, 1]](d: Double)") = forAll { (d: Double) =>
    isValid[Interval.OpenClosed[0, 1]](d) ?= (d > 0.0 && d <= 1.0)
  }

  property("showExpr[Interval.OpenClosed[0, 1]](0.5)") = secure {
    val s = showExpr[Interval.OpenClosed[0, 1]](0.5)
    (s ?= "((0.5 > 0) && !(0.5 > 1))") || (s ?= "((0.5 > 0.0) && !(0.5 > 1.0))")
  }

  property("isValid[Interval.ClosedOpen[0, 1]](d: Double)") = forAll { (d: Double) =>
    isValid[Interval.ClosedOpen[0, 1]](d) ?= (d >= 0.0 && d < 1.0)
  }

  property("showExpr[Interval.ClosedOpen[0, 1]](0.5)") = secure {
    val s = showExpr[Interval.ClosedOpen[0, 1]](0.5)
    (s ?= "(!(0.5 < 0) && (0.5 < 1))") || (s ?= "(!(0.5 < 0.0) && (0.5 < 1.0))")
  }

  property("isValid[Interval.Closed[0, 1]](d: Double)") = forAll { (d: Double) =>
    isValid[Interval.Closed[0, 1]](d) ?= (d >= 0.0 && d <= 1.0)
  }

  property("showExpr[Interval.Closed[0, 1]](0.5)") = secure {
    val s = showExpr[Interval.Closed[0, 1]](0.5)
    (s ?= "(!(0.5 < 0) && !(0.5 > 1))") || (s ?= "(!(0.5 < 0.0) && !(0.5 > 1.0))")
  }

  val floatWithNaN: Gen[Float] = Gen.frequency(8 -> Arbitrary.arbitrary[Float], 2 -> Float.NaN)
  val doubleWithNaN: Gen[Double] = Gen.frequency(8 -> Arbitrary.arbitrary[Double], 2 -> Double.NaN)

  property("isValid[NonNaN](f: Float)") = forAll(floatWithNaN) { (f: Float) =>
    isValid[NonNaN](f) ?= !f.isNaN
  }

  property("showExpr[NonNaN](Float.NaN)") = secure {
    showExpr[NonNaN](Float.NaN) ?= "(NaN != NaN)"
  }

  property("isValid[NonNaN](d: Double)") = forAll(doubleWithNaN) { (d: Double) =>
    isValid[NonNaN](d) ?= !d.isNaN
  }

  property("showExpr[NonNaN](Double.NaN)") = secure {
    showExpr[NonNaN](Double.NaN) ?= "(NaN != NaN)"
  }
}
