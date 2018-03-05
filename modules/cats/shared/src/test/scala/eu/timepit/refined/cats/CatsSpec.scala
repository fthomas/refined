package eu.timepit.refined.cats

import cats.implicits._
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
}
