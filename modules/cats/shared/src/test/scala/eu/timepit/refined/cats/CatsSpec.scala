package eu.timepit.refined.cats

import cats.instances.int._
import cats.syntax.eq._
import cats.syntax.show._
import eu.timepit.refined.auto._
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties

class CatsSpec extends Properties("cats") {

  property("Equal") = secure {
    val x: PosInt = 5
    val y: PosInt = 5
    x === y
  }

  property("Show") = secure {
    val x: PosInt = 5
    x.show ?= "5"
  }
}
