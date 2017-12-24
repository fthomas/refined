package eu.timepit.refined

import eu.timepit.refined.api.{Min, Refined, RefinedTypeOps}
import eu.timepit.refined.boolean._
import eu.timepit.refined.numeric._
import eu.timepit.refined.types.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class MinSpec extends Properties("Min") {

  property("Min[Int Refined Greater[W.`1`.T]]") = secure {
    Min[Int Refined Greater[W.`1`.T]].min =? refineMV(2)
  }

  property("Min[Int Refined Greater[_0]]") = secure {
    Min[Int Refined Greater[_0]].min =? refineMV(1)
  }

  property("Min[Long Refined Greater[_0]]") = secure {
    Min[Long Refined Greater[_0]].min =? refineMV(1L)
  }

  property("Min[Float Refined Greater[_0]]") = secure {
    Min[Float Refined Greater[W.`0f`.T]].min =? Refined.unsafeApply("1.4E-45".toFloat)
  }

  property("Min[Byte Refined Less[_0]]") = secure {
    Min[Byte Refined Less[_0]].min =? refineMV(Byte.MinValue)
  }

  property("Min[Short Refined Less[_0]]") = secure {
    Min[Short Refined Less[_0]].min =? refineMV(Short.MinValue)
  }

  property("Min[Int Refined Less[_0]]") = secure {
    Min[Int Refined Less[_0]].min =? refineMV(Int.MinValue)
  }

  property("Min[Long Refined Less[_0]]") = secure {
    Min[Long Refined Less[_0]].min =? refineMV(Long.MinValue)
  }

  property("Min[Float Refined Less[_0]]") = secure {
    Min[Float Refined Less[_0]].min =? Refined.unsafeApply(Float.MinValue)
  }

  property("Min[Double Refined Less[_0]]") = secure {
    Min[Double Refined Less[_0]].min =? Refined.unsafeApply(Double.MinValue)
  }

  property("Min[Int Refined NonPositive]") = secure {
    Min[Int Refined NonPositive].min =? refineMV(Int.MinValue)
  }

  property("Min[Int Refined NonNegative]") = secure {
    Min[Int Refined NonNegative].min =? refineMV(0)
  }

  property("Min[Float Refined NonNegative]") = secure {
    Min[Float Refined NonNegative].min =? refineMV(0f)
  }

  property("Min[Double Refined NonNegative]") = secure {
    Min[Double Refined NonNegative].min =? refineMV(0f)
  }

  property("Min[Int Refined Not[Less[W.`-5`.T]]]") = secure {
    Min[Int Refined Not[Less[W.`-5`.T]]].min =? refineMV(-5)
  }

  property("Min[Int Refined Interval.Open[_10, _20]]") = secure {
    Min[Int Refined Interval.Open[_10, _20]].min =? refineMV[Interval.Open[_10, _20]](11)
  }

  property("Min[Double Refined Interval.Open[_10, _20]]") = secure {
    Min[Double Refined Interval.Open[_10, _20]].min =? refineMV[Interval.Open[_10, _20]](
      10.000000000000002d)
  }

  property("Min[Int Refined Interval.Closed[W.`-20`.T, W.`10`.T]]") = secure {
    Min[Int Refined Interval.Closed[W.`-20`.T, W.`10`.T]].min =?
      refineMV[Interval.Closed[W.`-20`.T, W.`10`.T]](-20)
  }

  property("Min[Double Refined Interval.Closed[W.`-20.001d`.T, W.`0d`.T]]") = secure {
    Min[Double Refined Interval.Closed[W.`-20.001d`.T, W.`0d`.T]].min =?
      refineMV[Interval.Closed[W.`-20.001d`.T, W.`0d`.T]](-20.001d)
  }

  property("Min[Char Refined Interval.Closed[W.`'A'`.T, W.`'Z'`.T]]") = secure {
    Min[Char Refined Interval.Closed[W.`'A'`.T, W.`'Z'`.T]].min =?
      refineMV[Interval.Closed[W.`'A'`.T, W.`'Z'`.T]]('A')
  }

  property("CompanionObject.min - Positive - Long") = secure {
    PosLong.MinValue =? PosLong.unsafeFrom(1)
  }

  property("CompanionObject.min - Negative - Float") = secure {
    type NegFloat = Float Refined Negative
    object NegFloat extends RefinedTypeOps.Numeric[NegFloat, Float]
    NegFloat.MinValue =? NegFloat.unsafeFrom(Float.MinValue)
  }
}
