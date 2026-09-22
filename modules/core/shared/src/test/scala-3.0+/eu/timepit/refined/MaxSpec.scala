package eu.timepit.refined

import eu.timepit.refined.api.{Max, Refined}
import eu.timepit.refined.boolean._
import eu.timepit.refined.numeric._
import eu.timepit.refined.types.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class MaxSpec extends Properties("Max") {

  property("Max[Int Refined Less[1]]") = secure {
    Max[Int Refined Less[1]].max.value ?= 0
  }

  property("Max[Int Refined Less[0]]") = secure {
    Max[Int Refined Less[0]].max.value ?= -1
  }

  property("Max[Long Refined Less[5]]") = secure {
    Max[Long Refined Less[5]].max.value ?= 4L
  }

  property("Max[Float Refined Less[5]]") = secure {
    Max[Float Refined Less[5]].max.value ?= 4.9999995f
  }

  property("Max[Byte Refined Greater[0]]") = secure {
    Max[Byte Refined Greater[0]].max.value ?= Byte.MaxValue
  }

  property("Max[Short Refined Greater[0]]") = secure {
    Max[Short Refined Greater[0]].max.value ?= Short.MaxValue
  }

  property("Max[Int Refined Greater[0]]") = secure {
    Max[Int Refined Greater[0]].max.value ?= Int.MaxValue
  }

  property("Max[Long Refined Greater[0]]") = secure {
    Max[Long Refined Greater[0]].max.value ?= Long.MaxValue
  }

  property("Max[Float Refined Greater[0]]") = secure {
    Max[Float Refined Greater[0]].max.value ?= Float.MaxValue
  }

  property("Max[Double Refined Greater[0]]") = secure {
    Max[Double Refined Greater[0]].max.value ?= Double.MaxValue
  }

  property("Max[Int Refined NonNegative]") = secure {
    Max[Int Refined NonNegative].max.value ?= Int.MaxValue
  }

  property("Max[Int Refined NonPositive]") = secure {
    Max[Int Refined NonPositive].max.value ?= 0
  }

  property("Max[Float Refined NonPositive]") = secure {
    Max[Float Refined NonPositive].max.value ?= 0f
  }

  property("Max[Double Refined NonPositive]") = secure {
    Max[Double Refined NonPositive].max.value ?= 0d
  }

  property("Max[Int Refined Not[Greater[-5]]]") = secure {
    Max[Int Refined Not[Greater[-5]]].max.value ?= -5
  }

  property("Max[Int Refined Interval.Open[1, 4]]") = secure {
    Max[Int Refined Interval.Open[1, 4]].max.value ?= 3
  }

  property("Max[Double Refined Interval.Open[1, 4]]") = secure {
    Max[Double Refined Interval.Open[1, 4]].max.value ?= 3.9999999999999996
  }

  property("Max[Int Refined Interval.Closed[-20, 10]]") = secure {
    Max[Int Refined Interval.Closed[-20, 10]].max.value ?= 10
  }

  property("Max[Double Refined Interval.Closed[-20d, 10.99991d]]") = secure {
    Max[Double Refined Interval.Closed[-20d, 10.99991d]].max.value ?= 10.99991d
  }

  property("Max[Char Refined Interval.Closed['A', 'Z']]") = secure {
    Max[Char Refined Interval.Closed['A', 'Z']].max.value ?= 'Z'
  }

  property("Max[Int Refined Even]") = secure {
    Max[Int Refined Even].max.value ?= 2147483646
  }

  property("Max[Int Refined Divisible[5]]") = secure {
    Max[Int Refined Divisible[5]].max.value ?= 2147483645
  }

  property("Max[Int Refined (Negative And Even)]") = secure {
    Max[Int Refined (Negative And Even)].max.value ?= -2
  }

  property("NegLong.MaxValue") = secure {
    NegLong.MaxValue ?= NegLong.unsafeFrom(-1)
  }

  property("PosFloat.MaxValue") = secure {
    PosFloat.MaxValue ?= PosFloat.unsafeFrom(Float.MaxValue)
  }
}
