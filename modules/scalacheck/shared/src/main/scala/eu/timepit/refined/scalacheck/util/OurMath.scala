package eu.timepit.refined.scalacheck.util

import java.lang.{Double => JDouble}
import java.lang.{Float => JFloat}

// Delete this when Scala.js provides def nextAfter(a: Float, b: Double): Float
object OurMath {
  // scalastyle:off
  def nextAfter(start: Float, direction: Double): Float = {

    def incrBits = JFloat.intBitsToFloat(JFloat.floatToIntBits(start) + 1)
    def decrBits = JFloat.intBitsToFloat(JFloat.floatToIntBits(start) - 1)

    if (JFloat.isNaN(start) || JDouble.isNaN(direction)) {
      Float.NaN
    } else if (start == 0.0F && direction == 0.0) {
      direction.toFloat
    } else if (start == Float.NegativeInfinity && direction > start) {
      Float.MinValue
    } else if (start == Float.PositiveInfinity && direction < start) {
      Float.MaxValue
    } else if (start == Float.MinPositiveValue && direction < start) {
      0.0F
    } else if (start == -Float.MinPositiveValue && direction > start) {
      -0.0F
    } else if (start == Float.MaxValue && direction > start) {
      Float.PositiveInfinity
    } else if (start == Float.MinValue && direction < start) {
      Float.NegativeInfinity
    } else {
      if (direction > start) {
        if (start > 0) incrBits else if (start < 0) decrBits else Float.MinPositiveValue
      } else if (direction < start) {
        if (start > 0) decrBits else if (start < 0) incrBits else -Float.MinPositiveValue
      } else {
        direction.toFloat
      }
    }
  }
  // scalastyle:on
}
