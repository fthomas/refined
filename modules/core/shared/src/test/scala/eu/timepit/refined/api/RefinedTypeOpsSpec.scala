package eu.timepit.refined.api

import eu.timepit.refined.types.numeric.NonNegInt
import org.scalacheck.Prop._
import org.scalacheck.Properties
import scala.util.{Failure, Success, Try}

class RefinedTypeOpsSpec extends Properties("RefinedTypeOps") {

  property("from ~= unapply") = forAll { (i: Int) =>
    NonNegInt.from(i).toOption ?= NonNegInt.unapply(i)
  }

  property("from ~= unsafeFrom") = forAll { (i: Int) =>
    val stringOrNonNegInt = Try(NonNegInt.unsafeFrom(i)) match {
      case Success(n) => Right(n)
      case Failure(t) => Left(t.getMessage)
    }
    NonNegInt.from(i) ?= stringOrNonNegInt
  }
}
