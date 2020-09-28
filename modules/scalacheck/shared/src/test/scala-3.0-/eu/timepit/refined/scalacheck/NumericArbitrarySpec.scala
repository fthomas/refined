package eu.timepit.refined.scalacheck

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric._
import eu.timepit.refined.scalacheck.numeric._
import eu.timepit.refined.types.numeric._
import eu.timepit.refined.types.time.Minute
import org.scalacheck.Prop._
import org.scalacheck.Properties

class NumericArbitrarySpec extends Properties("NumericArbitrary") {

  property("Less[10]") = checkArbitraryRefinedType[Int Refined Less[W.`10`.T]]

  property("Less[-10]") = checkArbitraryRefinedType[Int Refined Less[W.`-10`.T]]

  property("LessEqual[42]") = checkArbitraryRefinedType[Int Refined LessEqual[W.`42`.T]]

  property("LessEqual[-42]") = checkArbitraryRefinedType[Int Refined LessEqual[W.`-42`.T]]

  property("Greater[10]") = checkArbitraryRefinedType[Int Refined Greater[W.`10`.T]]

  property("Greater[-10]") = checkArbitraryRefinedType[Int Refined Greater[W.`-10`.T]]

  property("GreaterEqual[123]") = checkArbitraryRefinedType[Int Refined GreaterEqual[W.`123`.T]]

  property("GreaterEqual[123]") = checkArbitraryRefinedType[Int Refined GreaterEqual[W.`-123`.T]]

  property("NonNaNFloat") = checkArbitraryRefinedType[NonNaNFloat]

  property("NonNaNDouble") = checkArbitraryRefinedType[NonNaNDouble]

  property("PosFloat") = checkArbitraryRefinedType[PosFloat]

  property("NonPositive") = checkArbitraryRefinedType[Short Refined NonPositive]

  property("NegDouble") = checkArbitraryRefinedType[NegDouble]

  property("NonNegLong") = checkArbitraryRefinedType[NonNegLong]

  property("Interval.Open[-23, 42]") =
    checkArbitraryRefinedType[Int Refined Interval.Open[W.`-23`.T, W.`42`.T]]

  property("Interval.Open[0.554, 0.556]") =
    checkArbitraryRefinedType[Double Refined Interval.Open[W.`0.554`.T, W.`0.556`.T]]

  property("Interval.OpenClosed[2.71828, 3.14159]") =
    checkArbitraryRefinedType[Double Refined Interval.OpenClosed[W.`2.71828`.T, W.`3.14159`.T]]

  property("Interval.OpenClosed[0.54, 0.56]") =
    checkArbitraryRefinedType[Float Refined Interval.OpenClosed[W.`0.54F`.T, W.`0.56F`.T]]

  property("Interval.ClosedOpen[-2.71828, 3.14159]") =
    checkArbitraryRefinedType[Double Refined Interval.ClosedOpen[W.`-2.71828`.T, W.`3.14159`.T]]

  property("Interval.ClosedOpen[0.4, 0.6]") =
    checkArbitraryRefinedType[Float Refined Interval.ClosedOpen[W.`0.4F`.T, W.`0.6F`.T]]

  property("Interval.Closed[23, 42]") =
    checkArbitraryRefinedType[Int Refined Interval.Closed[W.`23`.T, W.`42`.T]]

  property("Minute") = checkArbitraryRefinedType[Minute]

  property("chooseRefinedNum") =
    forAll(chooseRefinedNum(NonNegInt.unsafeFrom(23), NonNegInt.unsafeFrom(42))) { n =>
      n.value >= 23 && n.value <= 42
    }
}
