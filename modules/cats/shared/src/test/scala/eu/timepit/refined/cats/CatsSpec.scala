package eu.timepit.refined.cats

import cats.data.Validated
import cats.implicits._
//import eu.timepit.refined.W
//import eu.timepit.refined.api.{Refined, RefinedTypeOps}
//import eu.timepit.refined.numeric.Interval
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties

class CatsSpec extends Properties("cats") {

  property("Eq") = secure {
    val refTypeOrder: Unit = () // shadow the `Order` instance so the `Eq` instance is tested
    locally(refTypeOrder) // prevent a "unused" warning

    PosInt.unsafeFrom(5) === PosInt.unsafeFrom(5)
  }

  property("Order") = secure {
    val x = PosInt.unsafeFrom(5)
    val y = PosInt.unsafeFrom(6)
    x min y ?= x
  }

  property("Show") = secure {
    PosInt.unsafeFrom(5).show ?= "5"
  }

  property("Validate when Valid") = secure {
    import syntax._
    PosInt.validate(5) ?= Validated.Valid(PosInt.unsafeFrom(5))
  }

  property("Validate when Invalid") = secure {
    import syntax._
    PosInt.validate(-1) ?= Validated.invalidNel("Predicate failed: (-1 > 0).")
  }

  /*
  property("validate without import") = secure {
    type OneToTen = Int Refined Interval.Closed[W.`1`.T, W.`10`.T]
    object OneToTen extends RefinedTypeOps[OneToTen, Int] with CatsRefinedTypeOpsSyntax
    OneToTen.validate(5) ?= Validated.valid(OneToTen.unsafeFrom(5))
  }
 */
}
