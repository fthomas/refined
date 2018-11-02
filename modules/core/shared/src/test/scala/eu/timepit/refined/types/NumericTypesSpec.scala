package eu.timepit.refined.types

import eu.timepit.refined.types.all._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class NumericTypesSpec extends Properties("NumericTypes") {

  property("PosByte.from(1)") = secure {
    PosByte.from(1).isRight
  }

  property("PosByte.from(0)") = secure {
    PosByte.from(0) ?= Left("Predicate failed: (0 > 0).")
  }

  property("NonNegByte.from(0)") = secure {
    NonNegByte.from(0).isRight
  }

  property("NonNegByte.from(-1)") = secure {
    NonNegByte.from(-1) ?= Left("Predicate (-1 < 0) did not fail.")
  }

  property("NegByte.from(-1)") = secure {
    NegByte.from(-1).isRight
  }

  property("NegByte.from(0)") = secure {
    NegByte.from(0) ?= Left("Predicate failed: (0 < 0).")
  }

  property("NonPosByte.from(0)") = secure {
    NonPosByte.from(0).isRight
  }

  property("NonPosByte.from(1)") = secure {
    NonPosByte.from(1) ?= Left("Predicate (1 > 0) did not fail.")
  }

  property("PosShort.from(1)") = secure {
    PosShort.from(1).isRight
  }

  property("PosShort.from(0)") = secure {
    PosShort.from(0) ?= Left("Predicate failed: (0 > 0).")
  }

  property("NonNegShort.from(0)") = secure {
    NonNegShort.from(0).isRight
  }

  property("NonNegShort.from(-1)") = secure {
    NonNegShort.from(-1) ?= Left("Predicate (-1 < 0) did not fail.")
  }

  property("NegShort.from(-1)") = secure {
    NegShort.from(-1).isRight
  }

  property("NegShort.from(0)") = secure {
    NegShort.from(0) ?= Left("Predicate failed: (0 < 0).")
  }

  property("NonPosShort.from(0)") = secure {
    NonPosShort.from(0).isRight
  }

  property("NonPosShort.from(1)") = secure {
    NonPosShort.from(1) ?= Left("Predicate (1 > 0) did not fail.")
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

  property("PosBigInt.from(BigInt(1))") = secure {
    PosBigInt.from(BigInt(1)).isRight
  }

  property("PosBigInt.from(BigInt(0))") = secure {
    PosBigInt.from(BigInt(0)) ?= Left("Predicate failed: (0 > 0).")
  }

  property("NonNegBigInt.from(BigInt(0))") = secure {
    NonNegBigInt.from(BigInt(0)).isRight
  }

  property("NonNegBigInt.from(BigInt(-1))") = secure {
    NonNegBigInt.from(BigInt(-1)) ?= Left("Predicate (-1 < 0) did not fail.")
  }

  property("NegBigInt.from(BigInt(-1))") = secure {
    NegBigInt.from(BigInt(-1)).isRight
  }

  property("NegBigInt.from(BigInt(0))") = secure {
    NegBigInt.from(BigInt(0)) ?= Left("Predicate failed: (0 < 0).")
  }

  property("NonPosBigInt.from(BigInt(0))") = secure {
    NonPosBigInt.from(BigInt(0)).isRight
  }

  property("NonPosBigInt.from(BigInt(1))") = secure {
    NonPosBigInt.from(BigInt(1)) ?= Left("Predicate (1 > 0) did not fail.")
  }

  property("PosFloat.from(0.1F)") = secure {
    PosFloat.from(0.1f).isRight
  }

  property("PosFloat.from(0.0F)") = secure {
    PosFloat.from(0.0f).isLeft
  }

  property("NonNegFloat.from(0.0F)") = secure {
    NonNegFloat.from(0.0f).isRight
  }

  property("NonNegFloat.from(-0.1F)") = secure {
    NonNegFloat.from(-0.1f).isLeft
  }

  property("NegFloat.from(-0.1F)") = secure {
    NegFloat.from(-0.1f).isRight
  }

  property("NegFloat.from(0.0F)") = secure {
    NegFloat.from(0.0f).isLeft
  }

  property("NonPosFloat.from(0.0F)") = secure {
    NonPosFloat.from(0.0f).isRight
  }

  property("NonPosFloat.from(0.1F)") = secure {
    NonPosFloat.from(0.1f).isLeft
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

  property("PosBigDecimal.from(BigDecimal(0.1))") = secure {
    PosBigDecimal.from(BigDecimal(0.1)).isRight
  }

  property("PosBigDecimal.from(BigDecimal(0.0))") = secure {
    PosBigDecimal.from(BigDecimal(0.0)).isLeft
  }

  property("NonNegBigDecimal.from(BigDecimal(0.0))") = secure {
    NonNegBigDecimal.from(BigDecimal(0.0)).isRight
  }

  property("NonNegBigDecimal.from(BigDecimal(-0.1))") = secure {
    NonNegBigDecimal.from(BigDecimal(-0.1)).isLeft
  }

  property("NegBigDecimal.from(BigDecimal(-0.1))") = secure {
    NegBigDecimal.from(BigDecimal(-0.1)).isRight
  }

  property("NegBigDecimal.from(BigDecimal(0.0))") = secure {
    NegBigDecimal.from(BigDecimal(0.0)).isLeft
  }

  property("NonPosBigDecimal.from(BigDecimal(0.0))") = secure {
    NonPosBigDecimal.from(BigDecimal(0.0)).isRight
  }

  property("NonPosBigDecimal.from(BigDecimal(0.1))") = secure {
    NonPosBigDecimal.from(BigDecimal(0.1)).isLeft
  }
}
