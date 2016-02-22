package eu.timepit.refined.scalacheck.util

import org.scalacheck.Prop._
import org.scalacheck.Properties

class OurMathSpec extends Properties("OurMath") {

  property("OurMath.nextAfter == Math.nextAfter") = forAll { (start: Float, direction: Double) =>
    OurMath.nextAfter(start, direction) ?= Math.nextAfter(start, direction)
  }
}
