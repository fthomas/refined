package eu.timepit.refined

import eu.timepit.refined.api.{Max, Refined}
import eu.timepit.refined.boolean._
import eu.timepit.refined.numeric._
import eu.timepit.refined.types.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class MaxSpec extends Properties("Max") {

  property("Max[Int Refined Less[W.`1`.T]]") = secure {
    Max[Int Refined Less[W.`1`.T]].max =? refineMV(0)
  }

  property("Max[Int Refined Less[_0]]") = secure {
    Max[Int Refined Less[_0]].max =? refineMV(-1)
  }

  property("Max[Long Refined Less[_5]]") = secure {
    Max[Long Refined Less[_5]].max =? refineMV(4L)
  }

  property("Max[Byte Refined Greater[_0]]") = secure {
    Max[Byte Refined Greater[_0]].max =? refineMV(Byte.MaxValue)
  }

  property("Max[Short Refined Greater[_0]]") = secure {
    Max[Short Refined Greater[_0]].max =? refineMV(Short.MaxValue)
  }

  property("Max[Int Refined Greater[_0]]") = secure {
    Max[Int Refined Greater[_0]].max =? refineMV(Int.MaxValue)
  }

  property("Max[Long Refined Greater[_0]]") = secure {
    Max[Long Refined Greater[_0]].max =? refineMV(Long.MaxValue)
  }

  property("Max[Int Refined NonNegative]") = secure {
    Max[Int Refined NonNegative].max =? refineMV(Int.MaxValue)
  }

  property("Max[Int Refined NonPositive]") = secure {
    Max[Int Refined NonPositive].max =? refineMV(0)
  }

  property("Max[Int Refined Not[Greater[W.`-5`.T]]]") = secure {
    Max[Int Refined Not[Greater[W.`-5`.T]]].max =? refineMV(-5)
  }

  property("Max[Int Refined Interval.Open[_10, _20]]") = secure {
    Max[Int Refined Interval.Open[_10, _20]].max =? refineMV[Interval.Open[_10, _20]](19)
  }

  property("Max[Int Refined Interval.Closed[W.`-20`.T, W.`10`.T]]") = secure {
    Max[Int Refined Interval.Closed[W.`-20`.T, W.`10`.T]].max =?
      refineMV[Interval.Closed[W.`-20`.T, W.`10`.T]](10)
  }

  property("CompanionObject.max - Negative - Long") = secure {
    NegLong.max =? NegLong.unsafeFrom(-1)
  }
}
