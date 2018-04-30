package eu.timepit.refined.types

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.types.all._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class NumericTypesSpec extends Properties("NumericTypes") {

  property("PosInt.from(1)") = secure {
    PosInt.from(1).isRight
  }

  property("PosInt.from(0)") = secure {
    PosInt.from(0) ?= Left("Predicate failed: (0 > 0).")
  }

  property("PosInt.unapply(1)") = secure {
    val PosInt(x) = 1
    x ?= PosInt.unsafeFrom(1)
  }

  property("PosInt.unsafeFrom(1)") = wellTyped {
    PosInt.unsafeFrom(1)
  }

  property("PosInt.unsafeFrom(0)") = secure {
    throws(classOf[IllegalArgumentException])(PosInt.unsafeFrom(0))
  }

  property("NonNegInt.from(0)") = secure {
    NonNegInt.from(0).isRight
  }

  property("NonNegInt.from(-1)") = secure {
    NonNegInt.from(-1) ?= Left("Predicate (-1 < 0) did not fail.")
  }

  property("NonNegInt.unapply(0)") = secure {
    val NonNegInt(x) = 0
    x ?= NonNegInt.unsafeFrom(0)
  }

  property("NonNegInt.unsafeFrom(0)") = wellTyped {
    NonNegInt.unsafeFrom(0)
  }

  property("NonNegInt.unsafeFrom(-1)") = secure {
    throws(classOf[IllegalArgumentException])(NonNegInt.unsafeFrom(-1))
  }

  property("NegInt.from(-1)") = secure {
    NegInt.from(-1).isRight
  }

  property("NegInt.from(0)") = secure {
    NegInt.from(0) ?= Left("Predicate failed: (0 < 0).")
  }

  property("NegInt.unapply(-1)") = secure {
    val NegInt(x) = -1
    x ?= NegInt.unsafeFrom(-1)
  }

  property("NegInt.unsafeFrom(-1)") = wellTyped {
    NegInt.unsafeFrom(-1)
  }

  property("NegInt.unsafeFrom(0)") = secure {
    throws(classOf[IllegalArgumentException])(NegInt.unsafeFrom(0))
  }

  property("NonPosInt.from(0)") = secure {
    NonPosInt.from(0).isRight
  }

  property("NonPosInt.from(1)") = secure {
    NonPosInt.from(1) ?= Left("Predicate (1 > 0) did not fail.")
  }

  property("NonPosInt.unapply(0)") = secure {
    val NonPosInt(x) = 0
    x ?= NonPosInt.unsafeFrom(0)
  }

  property("NonPosInt.unsafeFrom(0)") = wellTyped {
    NonPosInt.unsafeFrom(0)
  }

  property("NonPosInt.unsafeFrom(1)") = secure {
    throws(classOf[IllegalArgumentException])(NonPosInt.unsafeFrom(1))
  }

  property("PosLong.from(1L)") = secure {
    PosLong.from(1L).isRight
  }

  property("PosLong.from(0L)") = secure {
    PosLong.from(0L) ?= Left("Predicate failed: (0 > 0).")
  }

  property("PosLong.unapply(1L)") = secure {
    val PosLong(x) = 1L
    x ?= PosLong.unsafeFrom(1L)
  }

  property("PosLong.unsafeFrom(1L)") = wellTyped {
    PosLong.unsafeFrom(1L)
  }

  property("PosLong.unsafeFrom(0L)") = secure {
    throws(classOf[IllegalArgumentException])(PosLong.unsafeFrom(0L))
  }

  property("NonNegLong.from(0L)") = secure {
    NonNegLong.from(0L).isRight
  }

  property("NonNegLong.from(-1L)") = secure {
    NonNegLong.from(-1L) ?= Left("Predicate (-1 < 0) did not fail.")
  }

  property("NonNegLong.unapply(0L)") = secure {
    val NonNegLong(x) = 0L
    x ?= NonNegLong.unsafeFrom(0L)
  }

  property("NonNegLong.unsafeFrom(0L)") = wellTyped {
    NonNegLong.unsafeFrom(0L)
  }

  property("NonNegLong.unsafeFrom(-1L)") = secure {
    throws(classOf[IllegalArgumentException])(NonNegLong.unsafeFrom(-1L))
  }

  property("NegLong.from(-1L)") = secure {
    NegLong.from(-1L).isRight
  }

  property("NegLong.from(0L)") = secure {
    NegLong.from(0L) ?= Left("Predicate failed: (0 < 0).")
  }

  property("NegLong.unapply(-1L)") = secure {
    val NegLong(x) = -1L
    x ?= NegLong.unsafeFrom(-1L)
  }

  property("NegLong.unsafeFrom(-1L)") = wellTyped {
    NegLong.unsafeFrom(-1L)
  }

  property("NegLong.unsafeFrom(0L)") = secure {
    throws(classOf[IllegalArgumentException])(NegLong.unsafeFrom(0L))
  }

  property("NonPosLong.from(0L)") = secure {
    NonPosLong.from(0L).isRight
  }

  property("NonPosLong.from(1L)") = secure {
    NonPosLong.from(1L) ?= Left("Predicate (1 > 0) did not fail.")
  }

  property("NonPosLong.unapply(0L)") = secure {
    val NonPosLong(x) = 0L
    x ?= NonPosLong.unsafeFrom(0L)
  }

  property("NonPosLong.unsafeFrom(0L)") = wellTyped {
    NonPosLong.unsafeFrom(0L)
  }

  property("NonPosLong.unsafeFrom(1L)") = secure {
    throws(classOf[IllegalArgumentException])(NonPosLong.unsafeFrom(1L))
  }

  property("PosFloat.from(0.1F)") = secure {
    PosFloat.from(0.1F).isRight
  }

  property("PosFloat.from(0.0F)") = secure {
    PosFloat.from(0.0F).isLeft
  }

  property("PosFloat.unapply(0.1F)") = secure {
    val PosFloat(x) = 0.1F
    x ?= PosFloat.unsafeFrom(0.1F)
  }

  property("PosFloat.unsafeFrom(0.1F)") = wellTyped {
    PosFloat.unsafeFrom(0.1F)
  }

  property("PosFloat.unsafeFrom(0.0F)") = secure {
    throws(classOf[IllegalArgumentException])(PosFloat.unsafeFrom(0.0F))
  }

  property("NonNegFloat.from(0.0F)") = secure {
    NonNegFloat.from(0.0F).isRight
  }

  property("NonNegFloat.from(-0.1F)") = secure {
    NonNegFloat.from(-0.1F).isLeft
  }

  property("NonNegFloat.unapply(0.0F)") = secure {
    val NonNegFloat(x) = 0.0F
    x ?= NonNegFloat.unsafeFrom(0.0F)
  }

  property("NonNegFloat.unsafeFrom(0.0F)") = wellTyped {
    NonNegFloat.unsafeFrom(0.0F)
  }

  property("NonNegFloat.unsafeFrom(-0.1F)") = secure {
    throws(classOf[IllegalArgumentException])(NonNegFloat.unsafeFrom(-0.1F))
  }

  property("NegFloat.from(-0.1F)") = secure {
    NegFloat.from(-0.1F).isRight
  }

  property("NegFloat.from(0.0F)") = secure {
    NegFloat.from(0.0F).isLeft
  }

  property("NegFloat.unapply(-0.1F)") = secure {
    val NegFloat(x) = -0.1F
    x ?= NegFloat.unsafeFrom(-0.1F)
  }

  property("NegFloat.unsafeFrom(-0.1F)") = wellTyped {
    NegFloat.unsafeFrom(-0.1F)
  }

  property("NegFloat.unsafeFrom(0.0F)") = secure {
    throws(classOf[IllegalArgumentException])(NegFloat.unsafeFrom(0.0F))
  }

  property("NonPosFloat.from(0.0F)") = secure {
    NonPosFloat.from(0.0F).isRight
  }

  property("NonPosFloat.from(0.1F)") = secure {
    NonPosFloat.from(0.1F).isLeft
  }

  property("NonPosFloat.unapply(0.0F)") = secure {
    val NonPosFloat(x) = 0.0F
    x ?= NonPosFloat.unsafeFrom(0.0F)
  }

  property("NonPosFloat.unsafeFrom(0.0F)") = wellTyped {
    NonPosFloat.unsafeFrom(0.0F)
  }

  property("NonPosFloat.unsafeFrom(0.1F)") = secure {
    throws(classOf[IllegalArgumentException])(NonPosFloat.unsafeFrom(0.1F))
  }

  property("PosDouble.from(0.1)") = secure {
    PosDouble.from(0.1).isRight
  }

  property("PosDouble.from(0.0)") = secure {
    PosDouble.from(0.0).isLeft
  }

  property("PosDouble.unapply(0.1)") = secure {
    val PosDouble(x) = 0.1
    x ?= PosDouble.unsafeFrom(0.1)
  }

  property("PosDouble.unsafeFrom(0.1)") = wellTyped {
    PosDouble.unsafeFrom(0.1)
  }

  property("PosDouble.unsafeFrom(0.0)") = secure {
    throws(classOf[IllegalArgumentException])(PosDouble.unsafeFrom(0.0))
  }

  property("NonNegDouble.from(0.0)") = secure {
    NonNegDouble.from(0.0).isRight
  }

  property("NonNegDouble.from(-0.1)") = secure {
    NonNegDouble.from(-0.1).isLeft
  }

  property("NonNegDouble.unapply(0.0)") = secure {
    val NonNegDouble(x) = 0.0
    x ?= NonNegDouble.unsafeFrom(0.0)
  }

  property("NonNegDouble.unsafeFrom(0.0)") = wellTyped {
    NonNegDouble.unsafeFrom(0.0)
  }

  property("NonNegDouble.unsafeFrom(-0.1)") = secure {
    throws(classOf[IllegalArgumentException])(NonNegDouble.unsafeFrom(-0.1))
  }

  property("NegDouble.from(-0.1)") = secure {
    NegDouble.from(-0.1).isRight
  }

  property("NegDouble.from(0.0)") = secure {
    NegDouble.from(0.0).isLeft
  }

  property("NegDouble.unapply(-0.1)") = secure {
    val NegDouble(x) = -0.1
    x ?= NegDouble.unsafeFrom(-0.1)
  }

  property("NegDouble.unsafeFrom(-0.1)") = wellTyped {
    NegDouble.unsafeFrom(-0.1)
  }

  property("NegDouble.unsafeFrom(0.0)") = secure {
    throws(classOf[IllegalArgumentException])(NegDouble.unsafeFrom(0.0))
  }

  property("NonPosDouble.from(0.0)") = secure {
    NonPosDouble.from(0.0).isRight
  }

  property("NonPosDouble.from(0.1)") = secure {
    NonPosDouble.from(0.1).isLeft
  }

  property("NonPosDouble.unapply(0.0)") = secure {
    val NonPosDouble(x) = 0.0
    x ?= NonPosDouble.unsafeFrom(0.0)
  }

  property("NonPosDouble.unsafeFrom(0.0)") = wellTyped {
    NonPosDouble.unsafeFrom(0.0)
  }

  property("NonPosDouble.unsafeFrom(0.1)") = secure {
    throws(classOf[IllegalArgumentException])(NonPosDouble.unsafeFrom(0.1))
  }
}
