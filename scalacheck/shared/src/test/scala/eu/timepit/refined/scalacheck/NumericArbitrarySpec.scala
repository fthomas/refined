package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.{ Greater, Less, Interval }
import eu.timepit.refined.scalacheck.numericArbitrary._
import eu.timepit.refined.util.time.Minute
import org.scalacheck.Prop._
import org.scalacheck.Properties

class NumericArbitrarySpec extends Properties("NumericArbitrary") {

  property("Less.positive") = forAll { (i: Int Refined Less[W.`100`.T]) =>
    i >= Int.MinValue && i < 100
  }

  property("Less.negative") = forAll { (i: Int Refined Less[W.`-100`.T]) =>
    i >= Int.MinValue && i < -100
  }

  property("Greater.positive") = forAll { (i: Int Refined Greater[W.`100`.T]) =>
    i > 100 && i <= Int.MaxValue
  }

  property("Greater.negative") = forAll { (i: Int Refined Greater[W.`-100`.T]) =>
    i > -100 && i <= Int.MaxValue
  }

  property("Interval") = forAll { (i: Int Refined Interval[W.`0`.T, W.`100`.T]) =>
    i >= 0 && i <= 100
  }

  property("Interval.alias") = forAll { m: Minute =>
    m >= 0 && m <= 59
  }
}
