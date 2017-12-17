package eu.timepit.refined.types

import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties

class NumericTypesSpec extends Properties("NumericTypes") {

  property("PosInt.from(1)") = secure {
    PosInt.from(1) ?= Right(PosInt(1))
  }

  property("PosInt.from(-1)") = secure {
    PosInt.from(-1) ?= Left("Predicate failed: (-1 > 0).")
  }

  property("PosInt.unapply(1)") = secure {
    val PosInt(x) = 1
    x ?= PosInt(1)
  }

  property("PosInt.unsafeFrom(1)") = secure {
    PosInt.unsafeFrom(1) ?= PosInt(1)
  }

  property("PosInt.unsafeFrom(-1)") = secure {
    throws(classOf[IllegalArgumentException])(PosInt.unsafeFrom(-1))
  }
}
