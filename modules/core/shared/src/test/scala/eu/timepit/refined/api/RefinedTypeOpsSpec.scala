package eu.timepit.refined.api

import eu.timepit.refined.types.numeric.NonNegInt
import org.scalacheck.Prop._
import org.scalacheck.Properties
import scala.util.Try

class RefinedTypeOpsSpec extends Properties("RefinedTypeOps") {

  property("from ~= unapply") = forAll { i: Int =>
    NonNegInt.from(i).right.toOption ?= NonNegInt.unapply(i)
  }

  property("from ~= unsafeFrom") = forAll { i: Int =>
    NonNegInt.from(i) ?= Try(NonNegInt.unsafeFrom(i)).toEither.left.map(_.getMessage)
  }
}
