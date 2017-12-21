package eu.timepit.refined.scalaz

import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties
import scalaz.Validation

class ScalazSpec extends Properties("scalaz") {
  property("Validate when Valid") = secure {
    import validation._
    PosInt.validate(5) ?= Validation.success(PosInt.unsafeFrom(5))
  }

  property("Validate when Invalid") = secure {
    import validation._
    PosInt.validate(0) ?= Validation.failureNel("Predicate failed: (0 > 0).")
  }

  property("Validate as syntax on the raw type") = secure {
    import validation._
    val x = 5
    x.validate[Positive] ?= Validation.success(PosInt.unsafeFrom(5))
  }
}
