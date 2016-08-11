package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import eu.timepit.refined.scalacheck.numeric._
import eu.timepit.refined.util.time.Minute
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class NumericArbitrarySpec extends Properties("NumericArbitrary") {

  property("Less.positive") =
    checkArbitraryRefinedType[Int Refined Less[W.`100`.T]]

  property("Less.negative") =
    checkArbitraryRefinedType[Int Refined Less[W.`-100`.T]]

  property("Less.Nat") =
    checkArbitraryRefinedType[Long Refined Less[_10]]

  property("LessEqual.positive") =
    checkArbitraryRefinedType[Int Refined LessEqual[W.`42`.T]]

  property("LessEqual.negative") =
    checkArbitraryRefinedType[Int Refined LessEqual[W.`-42`.T]]

  property("LessEqual.Nat") =
    checkArbitraryRefinedType[Long Refined LessEqual[_10]]

  property("Greater.positive") =
    checkArbitraryRefinedType[Int Refined Greater[W.`100`.T]]

  property("Greater.negative") =
    checkArbitraryRefinedType[Int Refined Greater[W.`-100`.T]]

  property("Greater.Nat") =
    checkArbitraryRefinedType[Long Refined Greater[_10]]

  property("GreaterEqual.positive") =
    checkArbitraryRefinedType[Int Refined GreaterEqual[W.`123456`.T]]

  property("GreaterEqual.negative") =
    checkArbitraryRefinedType[Int Refined GreaterEqual[W.`-123456`.T]]

  property("GreaterEqual.Nat") =
    checkArbitraryRefinedType[Int Refined GreaterEqual[_10]]

  property("Positive") =
    checkArbitraryRefinedType[Float Refined Positive]

  property("NonPositive") =
    checkArbitraryRefinedType[Short Refined NonPositive]

  property("Negative") =
    checkArbitraryRefinedType[Double Refined Negative]

  property("NonNegative") =
    checkArbitraryRefinedType[Long Refined NonNegative]

  property("Interval.Open") =
    checkArbitraryRefinedType[Int Refined Interval.Open[W.`-23`.T, W.`42`.T]]

  property("Interval.Open (0.554, 0.556)") =
    checkArbitraryRefinedType[Double Refined Interval.Open[W.`0.554`.T, W.`0.556`.T]]

  property("Interval.OpenClosed") =
    checkArbitraryRefinedType[Double Refined Interval.OpenClosed[W.`2.71828`.T, W.`3.14159`.T]]

  property("Interval.OpenClosed (0.54, 0.56]") =
    checkArbitraryRefinedType[Float Refined Interval.OpenClosed[W.`0.54F`.T, W.`0.56F`.T]]

  property("Interval.ClosedOpen") =
    checkArbitraryRefinedType[Double Refined Interval.ClosedOpen[W.`-2.71828`.T, W.`3.14159`.T]]

  property("Interval.ClosedOpen [0.4, 0.6)") =
    checkArbitraryRefinedType[Float Refined Interval.ClosedOpen[W.`0.4F`.T, W.`0.6F`.T]]

  property("Interval.Closed") =
    checkArbitraryRefinedType[Int Refined Interval.Closed[W.`23`.T, W.`42`.T]]

  property("Interval.alias") =
    checkArbitraryRefinedType[Minute]

  property("chooseRefinedNum") = {
    type Natural = Int Refined NonNegative
    forAll(chooseRefinedNum(23: Natural, 42: Natural)) { n =>
      n >= 23 && n <= 42
    }
  }
}
