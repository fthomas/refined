package eu.timepit.refined

import eu.timepit.refined.api.{Min, Refined}
import eu.timepit.refined.boolean._
import eu.timepit.refined.numeric._
import eu.timepit.refined.types.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.tag.@@

class MinSpec extends Properties("Min") {

  property("Min[Int Refined Greater[1]]") = secure {
    Min[Int Refined Greater[W.`1`.T]].min.value ?= 2
  }

  property("Min[Int Refined Greater[0]]") = secure {
    Min[Int Refined Greater[W.`0`.T]].min.value ?= 1
  }

  property("Min[Long Refined Greater[0]]") = secure {
    Min[Long Refined Greater[W.`0`.T]].min.value ?= 1L
  }

  property("Min[Float Refined Greater[0]]") = secure {
    Min[Float Refined Greater[W.`0f`.T]].min.value ?= "1.4E-45".toFloat
  }

  property("Min[Byte Refined Less[0]]") = secure {
    Min[Byte Refined Less[W.`0`.T]].min.value ?= Byte.MinValue
  }

  property("Min[Short Refined Less[0]]") = secure {
    Min[Short Refined Less[W.`0`.T]].min.value ?= Short.MinValue
  }

  property("Min[Int Refined Less[0]]") = secure {
    Min[Int Refined Less[W.`0`.T]].min.value ?= Int.MinValue
  }

  property("Min[Long Refined Less[0]]") = secure {
    Min[Long Refined Less[W.`0`.T]].min.value ?= Long.MinValue
  }

  property("Min[Float Refined Less[0]]") = secure {
    Min[Float Refined Less[W.`0`.T]].min.value ?= Float.MinValue
  }

  property("Min[Double Refined Less[0]]") = secure {
    Min[Double Refined Less[W.`0`.T]].min.value ?= Double.MinValue
  }

  property("Min[Int Refined NonPositive]") = secure {
    Min[Int Refined NonPositive].min.value ?= Int.MinValue
  }

  property("Min[Int Refined NonNegative]") = secure {
    Min[Int Refined NonNegative].min.value ?= 0
  }

  property("Min[Float Refined NonNegative]") = secure {
    Min[Float Refined NonNegative].min.value ?= 0f
  }

  property("Min[Double Refined NonNegative]") = secure {
    Min[Double Refined NonNegative].min.value ?= 0f
  }

  property("Min[Int Refined Not[Less[-5]]]") = secure {
    Min[Int Refined Not[Less[W.`-5`.T]]].min.value ?= -5
  }

  property("Min[Int Refined Interval.Open[1, 4]]") = secure {
    Min[Int Refined Interval.Open[W.`1`.T, W.`4`.T]].min.value ?= 2
  }

  property("Min[Int @@ Interval.Open[1, 4]]") = secure {
    Min[Int @@ Interval.Open[W.`1`.T, W.`4`.T]].min.asInstanceOf[Int] ?= 2
  }

  property("Min[Double Refined Interval.Open[1, 4]]") = secure {
    Min[Double Refined Interval.Open[W.`1`.T, W.`4`.T]].min.value ?= 1.0000000000000002
  }

  property("Min[Int Refined Interval.Closed[-20, 10]]") = secure {
    Min[Int Refined Interval.Closed[W.`-20`.T, W.`10`.T]].min.value ?= -20
  }

  property("Min[Double Refined Interval.Closed[-20.001d, 0d]]") = secure {
    Min[Double Refined Interval.Closed[W.`-20.001d`.T, W.`0d`.T]].min.value ?= -20.001d
  }

  property("Min[Char Refined Interval.Closed['A', 'Z']]") = secure {
    Min[Char Refined Interval.Closed[W.`'A'`.T, W.`'Z'`.T]].min.value ?= 'A'
  }

  property("Min[Int Refined Even]") = secure {
    Min[Int Refined Even].min.value ?= Int.MinValue
  }

  property("Min[Int Refined Divisible[5]]") = secure {
    Min[Int Refined Divisible[W.`5`.T]].min.value ?= -2147483645
  }

  property("Min[Int Refined (Positive And Even)]") = secure {
    Min[Int Refined (Positive And Even)].min.value ?= 2
  }

  property("PosLong.MinValue") = secure {
    PosLong.MinValue ?= PosLong.unsafeFrom(1)
  }

  property("NegFloat.MinValue") = secure {
    NegFloat.MinValue ?= NegFloat.unsafeFrom(Float.MinValue)
  }
}
