package eu.timepit.refined.scalacheck

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import eu.timepit.refined.scalacheck.numeric._
import eu.timepit.refined.types.time.Minute
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.tag.@@

class NumericArbitrarySpec extends Properties("NumericArbitrary") {

  property("Less.positive") = checkArbitraryRefType[Refined, Int, Less[W.`100`.T]]

  property("Less.negative") = checkArbitraryRefType[Refined, Int, Less[W.`-100`.T]]

  property("Less.Nat") = checkArbitraryRefType[Refined, Long, Less[_10]]

  property("LessEqual.positive") = checkArbitraryRefType[Refined, Int, LessEqual[W.`42`.T]]

  property("LessEqual.negative") = checkArbitraryRefType[Refined, Int, LessEqual[W.`-42`.T]]

  property("LessEqual.Nat") = checkArbitraryRefType[Refined, Long, LessEqual[_10]]

  property("Greater.positive") = checkArbitraryRefType[Refined, Int, Greater[W.`100`.T]]

  property("Greater.negative") = checkArbitraryRefType[Refined, Int, Greater[W.`-100`.T]]

  property("Greater.Nat") = checkArbitraryRefType[Refined, Long, Greater[_10]]

  property("Greater.Nat - Tag") = checkArbitraryRefType[@@, Long, Greater[_10]]

  property("GreaterEqual.positive") =
    checkArbitraryRefType[Refined, Int, GreaterEqual[W.`123456`.T]]

  property("GreaterEqual.negative") =
    checkArbitraryRefType[Refined, Int, GreaterEqual[W.`-123456`.T]]

  property("GreaterEqual.Nat") = checkArbitraryRefType[Refined, Int, GreaterEqual[_10]]

  property("Positive") = checkArbitraryRefType[Refined, Float, Positive]

  property("NonPositive") = checkArbitraryRefType[Refined, Short, NonPositive]

  property("Negative") = checkArbitraryRefType[Refined, Double, Negative]

  property("NonNegative") = checkArbitraryRefType[Refined, Long, NonNegative]

  property("Interval.Open") =
    checkArbitraryRefType[Refined, Int, Interval.Open[W.`-23`.T, W.`42`.T]]

  property("Interval.Open (0.554, 0.556)") =
    checkArbitraryRefType[Refined, Double, Interval.Open[W.`0.554`.T, W.`0.556`.T]]

  property("Interval.OpenClosed") =
    checkArbitraryRefType[Refined, Double, Interval.OpenClosed[W.`2.71828`.T, W.`3.14159`.T]]

  property("Interval.OpenClosed (0.54, 0.56]") =
    checkArbitraryRefType[Refined, Float, Interval.OpenClosed[W.`0.54F`.T, W.`0.56F`.T]]

  property("Interval.ClosedOpen") =
    checkArbitraryRefType[Refined, Double, Interval.ClosedOpen[W.`-2.71828`.T, W.`3.14159`.T]]

  property("Interval.ClosedOpen [0.4, 0.6)") =
    checkArbitraryRefType[Refined, Float, Interval.ClosedOpen[W.`0.4F`.T, W.`0.6F`.T]]

  property("Interval.Closed") =
    checkArbitraryRefType[Refined, Int, Interval.Closed[W.`23`.T, W.`42`.T]]

  property("Interval.alias") = forAll { m: Minute =>
    m >= 0 && m <= 59
  }

  property("chooseRefinedNum") = {
    type Natural = Int Refined NonNegative
    forAll(chooseRefinedNum(23: Natural, 42: Natural)) { n =>
      n >= 23 && n <= 42
    }
  }
}
