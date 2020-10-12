package eu.timepit.refined.cats

import _root_.cats.data.{NonEmptyList, NonEmptyVector, Validated}
import eu.timepit.refined.W
import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.numeric.Interval
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties

class SyntaxSpec extends Properties("syntax") {

  property("ValidateNel when Valid") = secure {
    import syntax._
    PosInt.validateNel(5) ?= Validated.Valid(PosInt.unsafeFrom(5))
  }

  property("ValidateNel when Invalid") = secure {
    import syntax._
    PosInt.validateNel(-1) ?= Validated.invalidNel("Predicate failed: (-1 > 0).")
  }

  property("validateNel without import") = secure {
    type OneToTen = Int Refined Interval.Closed[W.`1`.T, W.`10`.T]
    object OneToTen extends RefinedTypeOps[OneToTen, Int] with CatsRefinedTypeOpsSyntax
    OneToTen.validateNel(5) ?= Validated.valid(OneToTen.unsafeFrom(5))
  }

  property("ValidateNec when Valid") = secure {
    import syntax._
    PosInt.validateNec(5) ?= Validated.Valid(PosInt.unsafeFrom(5))
  }

  property("ValidateNec when Invalid") = secure {
    import syntax._
    PosInt.validateNec(-1) ?= Validated.invalidNec("Predicate failed: (-1 > 0).")
  }

  property("validateNec without import") = secure {
    type OneToTen = Int Refined Interval.Closed[W.`1`.T, W.`10`.T]
    object OneToTen extends RefinedTypeOps[OneToTen, Int] with CatsRefinedTypeOpsSyntax
    OneToTen.validateNec(5) ?= Validated.valid(OneToTen.unsafeFrom(5))
  }

  property("NonEmptyList refinedSize (1)") = secure {
    import syntax._
    NonEmptyList.of("one").refinedSize ?= PosInt.unsafeFrom(1)
  }

  property("NonEmptyList refinedSize (> 1)") = secure {
    import syntax._
    NonEmptyList.of("one", "two", "three").refinedSize ?= PosInt.unsafeFrom(3)
  }

  property("NonEmptyList take (== size)") = secure {
    import syntax._
    NonEmptyList.of("one").take(PosInt.unsafeFrom(1)) ?= NonEmptyList.of("one")
  }

  property("NonEmptyList take (> size)") = secure {
    import syntax._
    NonEmptyList.of("one").take(PosInt.unsafeFrom(2)) ?= NonEmptyList.of("one")
  }

  property("NonEmptyList take (< size)") = secure {
    import syntax._
    NonEmptyList.of("one", "two", "three").take(PosInt.unsafeFrom(2)) ?= NonEmptyList.of("one", "two")
  }

  property("NonEmptyVector take (== size)") = secure {
    import syntax._
    NonEmptyVector.of("one").take(PosInt.unsafeFrom(1)) ?= NonEmptyVector.of("one")
  }

  property("NonEmptyVector take (> size)") = secure {
    import syntax._
    NonEmptyVector.of("one").take(PosInt.unsafeFrom(2)) ?= NonEmptyVector.of("one")
  }

  property("NonEmptyVector take (< size)") = secure {
    import syntax._
    NonEmptyVector.of("one", "two", "three").take(PosInt.unsafeFrom(2)) ?= NonEmptyVector.of("one", "two")
  }
}
