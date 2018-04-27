package eu.timepit.refined

import eu.timepit.refined.api.{Max, Refined, RefinedTypeOps}
import eu.timepit.refined.boolean._
import eu.timepit.refined.numeric._
import eu.timepit.refined.types.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.tag.@@

class MaxSpec extends Properties("Max") {

  property("Max[Int Refined Less[1]]") = secure {
    Max[Int Refined Less[1]].max =? Refined.unsafeApply(0)
  }

  property("Max[Int Refined Less[_0]]") = secure {
    Max[Int Refined Less[_0]].max =? Refined.unsafeApply(-1)
  }

  property("Max[Long Refined Less[_5]]") = secure {
    Max[Long Refined Less[_5]].max =? Refined.unsafeApply(4L)
  }

  property("Max[Float Refined Less[_5]]") = secure {
    Max[Float Refined Less[_5]].max =? Refined.unsafeApply(4.9999995f)
  }

  property("Max[Byte Refined Greater[_0]]") = secure {
    Max[Byte Refined Greater[_0]].max =? Refined.unsafeApply(Byte.MaxValue)
  }

  property("Max[Short Refined Greater[_0]]") = secure {
    Max[Short Refined Greater[_0]].max =? Refined.unsafeApply(Short.MaxValue)
  }

  property("Max[Int Refined Greater[_0]]") = secure {
    Max[Int Refined Greater[_0]].max =? Refined.unsafeApply(Int.MaxValue)
  }

  property("Max[Long Refined Greater[_0]]") = secure {
    Max[Long Refined Greater[_0]].max =? Refined.unsafeApply(Long.MaxValue)
  }

  property("Max[Float Refined Greater[_0]]") = secure {
    Max[Float Refined Greater[_0]].max =? Refined.unsafeApply(Float.MaxValue)
  }

  property("Max[Double Refined Greater[_0]]") = secure {
    Max[Double Refined Greater[_0]].max =? Refined.unsafeApply(Double.MaxValue)
  }

  property("Max[Int Refined NonNegative]") = secure {
    Max[Int Refined NonNegative].max =? Refined.unsafeApply(Int.MaxValue)
  }

  property("Max[Int Refined NonPositive]") = secure {
    Max[Int Refined NonPositive].max =? Refined.unsafeApply(0)
  }

  property("Max[Float Refined NonPositive]") = secure {
    Max[Float Refined NonPositive].max =? Refined.unsafeApply(0f)
  }

  property("Max[Double Refined NonPositive]") = secure {
    Max[Double Refined NonPositive].max =? Refined.unsafeApply(0d)
  }

  property("Max[Int Refined Not[Greater[-5]]]") = secure {
    Max[Int Refined Not[Greater[-5]]].max =? Refined.unsafeApply(-5)
  }

  property("Max[Int Refined Interval.Open[_10, _20]]") = secure {
    Max[Int Refined Interval.Open[_10, _20]].max =? Refined.unsafeApply(19)
  }

  property("Max[Double Refined Interval.Open[_10, _20]]") = secure {
    Max[Double Refined Interval.Open[_10, _20]].max =? Refined.unsafeApply(19.999999999999996d)
  }

  property("Max[Int Refined Interval.Closed[-20, 10]]") = secure {
    Max[Int Refined Interval.Closed[-20, 10]].max =? Refined.unsafeApply(10)
  }

  property("Max[Int @@ Interval.Closed[-20, 10]]") = secure {
    Max[Int @@ Interval.Closed[-20, 10]].max =?
      refineMT[Interval.Closed[-20, 10]](10)
  }

  property("Max[Double Refined Interval.Closed[-20, 10]]") = secure {
    Max[Double Refined Interval.Closed[-20d, 10.99991d]].max =?
      Refined.unsafeApply(10.99991d)
  }

  property("Max[Char Refined Interval.Closed['A', 'Z']]") = secure {
    Max[Char Refined Interval.Closed['A', 'Z']].max =? Refined.unsafeApply('Z')
  }

  property("Max[Int Refined Even]") = secure {
    Max[Int Refined Even].max =? Refined.unsafeApply(2147483646)
  }

  property("Max[Int Refined Divisible[_5]]") = secure {
    Max[Int Refined Divisible[_5]].max =? Refined.unsafeApply(2147483645)
  }

  property("Max[Int Refined (Negative And Even)]") = secure {
    Max[Int Refined (Negative And Even)].max =? Refined.unsafeApply(-2)
  }

  property("CompanionObject.max - Negative - Long") = secure {
    NegLong.MaxValue =? NegLong.unsafeFrom(-1)
  }

  property("CompanionObject.max - Positive - Float") = secure {
    type PosFloat = Float Refined Positive
    object PosFloat extends RefinedTypeOps.Numeric[PosFloat, Float]
    PosFloat.MaxValue =? PosFloat.unsafeFrom(Float.MaxValue)
  }
}
