package eu.timepit.refined.scalaz

import eu.timepit.refined.W
import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.numeric.Interval
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties
import scalaz.Validation

class ScalazSpec extends Properties("scalaz") {
  property("Validate when Valid") = secure {
    import syntax._
    PosInt.validate(5) ?= Validation.success(PosInt.unsafeFrom(5))
  }

  property("Validate when Invalid") = secure {
    import syntax._
    PosInt.validate(0) ?= Validation.failureNel("Predicate failed: (0 > 0).")
  }

  property("validate without import") = secure {
    type OneToTen = Int Refined Interval.Closed[W.`1`.T, W.`10`.T]
    object OneToTen extends RefinedTypeOps[OneToTen, Int] with ScalazRefinedTypeOpsSyntax
    OneToTen.validate(5) ?= Validation.success(OneToTen.unsafeFrom(5))
  }
}
