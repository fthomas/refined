package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.{ RefType, Validate, Refined }
import eu.timepit.refined.auto._
import eu.timepit.refined.boolean.Not
import eu.timepit.refined.numeric._
import eu.timepit.refined.scalacheck.numeric._
import eu.timepit.refined.util.time.Minute
import org.scalacheck.Prop._
import org.scalacheck.Properties

import shapeless.Nat

class NumericArbitrarySpec extends Properties("NumericArbitrary") {

  property("Less.positive") =
    checkArbitraryRefType[Refined, Int, Less[W.`100`.T]]

  property("Less.negative") =
    checkArbitraryRefType[Refined, Int, Less[W.`-100`.T]]

  property("Less.natural") =
    checkArbitraryRefType[Refined, Long, Less[Nat._10]]

  property("LessEqual.positive") =
    checkArbitraryRefType[Refined, Int, LessEqual[W.`42`.T]]

  property("LessEqual.negative") =
    checkArbitraryRefType[Refined, Int, LessEqual[W.`-42`.T]]

  property("LessEqual.natural") =
    checkArbitraryRefType[Refined, Long, LessEqual[Nat._10]]

  property("Greater.positive") =
    checkArbitraryRefType[Refined, Int, Greater[W.`100`.T]]

  property("Greater.negative") =
    checkArbitraryRefType[Refined, Int, Greater[W.`-100`.T]]

  property("Greater.natural") =
    checkArbitraryRefType[Refined, Long, Greater[Nat._10]]

  property("Greater range") =
    forAll { (min: Refined[Long, Greater[Nat._10]], max: Refined[Long, Greater[Nat._10]]) =>
      checkArbitraryRefType[Refined, Long, Greater[Nat._10]](
        greaterArbitraryRange[Refined, Long, Nat._10](min, max),
        implicitly[RefType[Refined]],
        Validate.fromPredicate((t: Long) => true, (t: Long) => t.toString, Greater[Nat._10](Nat(10)))
      )
    }

  property("GreaterEqual.positive") =
    checkArbitraryRefType[Refined, Int, GreaterEqual[W.`123456`.T]]

  property("GreaterEqual.negative") =
    checkArbitraryRefType[Refined, Int, GreaterEqual[W.`-123456`.T]]

  property("NotLess.natural") =
    checkArbitraryRefType[Refined, Long, Not[Less[Nat._10]]]

  property("Interval.Open") =
    checkArbitraryRefType[Refined, Int, Interval.Open[W.`-23`.T, W.`42`.T]]

  property("Interval.OpenClosed") =
    checkArbitraryRefType[Refined, Double, Interval.OpenClosed[W.`2.71828`.T, W.`3.14159`.T]]

  property("Interval.ClosedOpen") =
    checkArbitraryRefType[Refined, Double, Interval.ClosedOpen[W.`-2.71828`.T, W.`3.14159`.T]]

  property("Interval.Closed") =
    checkArbitraryRefType[Refined, Int, Interval.Closed[W.`23`.T, W.`42`.T]]

  property("Interval.alias") =
    forAll { m: Minute => m >= 0 && m <= 59 }
}
