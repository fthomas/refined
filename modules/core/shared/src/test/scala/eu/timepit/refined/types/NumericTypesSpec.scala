package eu.timepit.refined.types

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.types.all._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class NumericTypesSpec extends Properties("NumericTypes") {

  property("PosInt.from(1)") = secure {
    PosInt.from(1).isRight
  }

  property("PosInt.from(-1)") = secure {
    PosInt.from(-1) ?= Left("Predicate failed: (-1 > 0).")
  }

  property("PosInt.unapply(1)") = secure {
    val PosInt(x) = 1
    x ?= PosInt.unsafeFrom(1)
  }

  property("PosInt.unsafeFrom(1)") = wellTyped {
    PosInt.unsafeFrom(1)
  }

  property("PosInt.unsafeFrom(-1)") = secure {
    throws(classOf[IllegalArgumentException])(PosInt.unsafeFrom(-1))
  }

  property("NonNegInt.from(0)") = secure {
    NonNegInt.from(0).isRight
  }

  property("NegInt.from(-1)") = secure {
    NegInt.from(-1).isRight
  }

  property("NonPosInt.from(0)") = secure {
    NonPosInt.from(0).isRight
  }

  property("PosLong.from(1L)") = secure {
    PosLong.from(1L).isRight
  }

  property("NonNegLong.from(0L)") = secure {
    NonNegLong.from(0L).isRight
  }

  property("NegLong.from(-1L)") = secure {
    NegLong.from(-1L).isRight
  }

  property("NonPosLong.from(0L)") = secure {
    NonPosLong.from(0L).isRight
  }

  property("PosFloat.from(0.1F)") = secure {
    PosFloat.from(0.1F).isRight
  }

  property("NonNegFloat.from(0.0F)") = secure {
    NonNegFloat.from(0.0F).isRight
  }

  property("NegFloat.from(-0.1F)") = secure {
    NegFloat.from(-0.1F).isRight
  }

  property("NonPosFloat.from(0.0F)") = secure {
    NonPosFloat.from(0.0F).isRight
  }

  property("PosDouble.from(0.1)") = secure {
    PosDouble.from(0.1).isRight
  }

  property("NonNegDouble.from(0.0)") = secure {
    NonNegDouble.from(0.0).isRight
  }

  property("NegDouble.from(-0.1)") = secure {
    NegDouble.from(-0.1).isRight
  }

  property("NonPosDouble.from(0.0)") = secure {
    NonPosDouble.from(0.0).isRight
  }
}
