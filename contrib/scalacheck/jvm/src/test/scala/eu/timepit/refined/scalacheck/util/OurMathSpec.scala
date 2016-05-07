package eu.timepit.refined.scalacheck.util

import org.scalacheck.Prop._
import org.scalacheck.Properties

class OurMathSpec extends Properties("OurMath") {

  property("OurMath.nextAfter == Math.nextAfter (1)") = forAll {
    (start: Float, direction: Double) =>
      OurMath.nextAfter(start, direction) ?= Math.nextAfter(start, direction)
  }

  property("OurMath.nextAfter == Math.nextAfter (2)") = secure {
    val startValues = List(
        +0.0F,
        -0.0F,
        Float.NaN,
        Float.NegativeInfinity,
        Float.PositiveInfinity,
        Float.MinValue,
        Float.MaxValue,
        -Float.MinPositiveValue,
        +Float.MinPositiveValue
    )

    val directionValues = List(
        +0.0,
        -0.0,
        Double.NaN,
        Double.NegativeInfinity,
        Double.PositiveInfinity,
        Double.MinValue,
        Double.MaxValue,
        -Double.MinPositiveValue,
        +Double.MinPositiveValue
    )

    val combinations =
      startValues.flatMap(s => directionValues.map(d => (s, d)))
    combinations.forall {
      case (start, direction) =>
        val n1 = OurMath.nextAfter(start, direction)
        val n2 = Math.nextAfter(start, direction)
        n1 == n2 || (java.lang.Float.isNaN(n1) && java.lang.Float.isNaN(n2))
    }
  }
}
