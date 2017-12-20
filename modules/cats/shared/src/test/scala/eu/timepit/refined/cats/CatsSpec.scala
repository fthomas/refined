package eu.timepit.refined.cats

import cats.data.{NonEmptyList, Validated}
import cats.implicits._
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties

class CatsSpec extends Properties("cats") {

  property("Eq") = secure {
    val refTypeOrder: Unit = () // shadow the `Order` instance so the `Eq` instance is tested
    locally(refTypeOrder) // prevent a "unused" warning

    PosInt(5) === PosInt(5)
  }

  property("Order") = secure {
    val x = PosInt(5)
    val y = PosInt(6)
    x min y ?= x
  }

  property("Show") = secure {
    PosInt(5).show ?= "5"
  }

  property("Validate when Valid") = secure {
    import validation._
    PosInt.validate(5) ?= Validated.Valid(PosInt(5))
  }

  property("Validate when Invalid") = secure {
    import validation._
    PosInt.validate(0) ?= Validated.Invalid(NonEmptyList("Predicate failed: (0 > 0).", Nil))
  }
}
