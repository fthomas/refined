package eu.timepit.refined.types

import eu.timepit.refined.types.all._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class NumericTypesSpec extends Properties("NumericTypes") {

  property("PosByte.from(1: Byte)") = secure {
    PosByte.from(1: Byte).isRight
  }

  property("PosByte.from(-1: Byte)") = secure {
    PosByte.from(-1: Byte) ?= Left("Predicate failed: (-1 > 0).")
  }

  property("PosByte.unapply(1: Byte)") = secure {
    val PosByte(x) = 1: Byte
    x ?= PosByte.unsafeFrom(1: Byte)
  }

  property("PosByte.unsafeFrom(1: Byte)") = wellTyped {
    PosByte.unsafeFrom(1: Byte)
  }

  property("PosByte.unsafeFrom(-1: Byte)") = secure {
    throws(classOf[IllegalArgumentException])(PosByte.unsafeFrom(-1: Byte))
  }

  property("NonNegByte.from(0: Byte)") = secure {
    NonNegByte.from(0: Byte).isRight
  }

  property("NegByte.from(-1: Byte)") = secure {
    NegByte.from(-1: Byte).isRight
  }

  property("NonPosByte.from(0: Byte)") = secure {
    NonPosByte.from(0: Byte).isRight
  }

  property("PosInt.from(1)") = secure {
    PosInt.from(1).isRight
  }

  property("PosInt.from(0)") = secure {
    PosInt.from(0) ?= Left("Predicate failed: (0 > 0).")
  }

  property("NonNegInt.from(0)") = secure {
    NonNegInt.from(0).isRight
  }

  property("NonNegInt.from(-1)") = secure {
    NonNegInt.from(-1) ?= Left("Predicate (-1 < 0) did not fail.")
  }

  property("NegInt.from(-1)") = secure {
    NegInt.from(-1).isRight
  }

  property("NegInt.from(0)") = secure {
    NegInt.from(0) ?= Left("Predicate failed: (0 < 0).")
  }

  property("NonPosInt.from(0)") = secure {
    NonPosInt.from(0).isRight
  }

  property("NonPosInt.from(1)") = secure {
    NonPosInt.from(1) ?= Left("Predicate (1 > 0) did not fail.")
  }

  property("PosLong.from(1L)") = secure {
    PosLong.from(1L).isRight
  }

  property("PosLong.from(0L)") = secure {
    PosLong.from(0L) ?= Left("Predicate failed: (0 > 0).")
  }

  property("NonNegLong.from(0L)") = secure {
    NonNegLong.from(0L).isRight
  }

  property("NonNegLong.from(-1L)") = secure {
    NonNegLong.from(-1L) ?= Left("Predicate (-1 < 0) did not fail.")
  }

  property("NegLong.from(-1L)") = secure {
    NegLong.from(-1L).isRight
  }

  property("NegLong.from(0L)") = secure {
    NegLong.from(0L) ?= Left("Predicate failed: (0 < 0).")
  }

  property("NonPosLong.from(0L)") = secure {
    NonPosLong.from(0L).isRight
  }

  property("NonPosLong.from(1L)") = secure {
    NonPosLong.from(1L) ?= Left("Predicate (1 > 0) did not fail.")
  }

  property("PosFloat.from(0.1F)") = secure {
    PosFloat.from(0.1F).isRight
  }

  property("PosFloat.from(0.0F)") = secure {
    PosFloat.from(0.0F).isLeft
  }

  property("NonNegFloat.from(0.0F)") = secure {
    NonNegFloat.from(0.0F).isRight
  }

  property("NonNegFloat.from(-0.1F)") = secure {
    NonNegFloat.from(-0.1F).isLeft
  }

  property("NegFloat.from(-0.1F)") = secure {
    NegFloat.from(-0.1F).isRight
  }

  property("NegFloat.from(0.0F)") = secure {
    NegFloat.from(0.0F).isLeft
  }

  property("NonPosFloat.from(0.0F)") = secure {
    NonPosFloat.from(0.0F).isRight
  }

  property("NonPosFloat.from(0.1F)") = secure {
    NonPosFloat.from(0.1F).isLeft
  }

  property("PosDouble.from(0.1)") = secure {
    PosDouble.from(0.1).isRight
  }

  property("PosDouble.from(0.0)") = secure {
    PosDouble.from(0.0).isLeft
  }

  property("NonNegDouble.from(0.0)") = secure {
    NonNegDouble.from(0.0).isRight
  }

  property("NonNegDouble.from(-0.1)") = secure {
    NonNegDouble.from(-0.1).isLeft
  }

  property("NegDouble.from(-0.1)") = secure {
    NegDouble.from(-0.1).isRight
  }

  property("NegDouble.from(0.0)") = secure {
    NegDouble.from(0.0).isLeft
  }

  property("NonPosDouble.from(0.0)") = secure {
    NonPosDouble.from(0.0).isRight
  }

  property("NonPosDouble.from(0.1)") = secure {
    NonPosDouble.from(0.1).isLeft
  }
}
