package eu.timepit.refined.cats

import cats.instances.int._
import cats.syntax.order._
import cats.syntax.show._
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties

class CatsSpec extends Properties("cats") {

  property("Equal") = secure {
    PosInt.unsafeFrom(5) === PosInt.unsafeFrom(5)
  }

  property("Show") = secure {
    PosInt.unsafeFrom(5).show ?= "5"
  }

  property("Order") = secure {
    val x: PosInt = PosInt.unsafeFrom(5)
    val y: PosInt = PosInt.unsafeFrom(6)
    x min y ?= x
  }
}
