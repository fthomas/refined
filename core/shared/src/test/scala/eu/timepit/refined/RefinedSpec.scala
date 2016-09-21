package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.test.illTyped

class RefinedSpec extends Properties("Refined") {
  type NonEmptyString = String Refined NonEmpty

  property("apply") = wellTyped {
    illTyped(
      """ val x: NonEmptyString = Refined("") """,
      "eu.timepit.refined.api.Refined.type does not take parameters"
    )
  }

  property("copy") = wellTyped {
    val nonEmptyString1: NonEmptyString = refineMV[NonEmpty]("abc")
    illTyped(
      """ nonEmptyString1.copy("") """,
      "value copy is not a member of RefinedSpec\\.this\\.NonEmptyString"
    )
  }

  property("equals") = secure {
    (Refined.unsafeApply(1) ?= Refined.unsafeApply(1)) &&
    !Refined.unsafeApply(1).equals(1)
  }

  property("hashCode") = forAll { i: Int =>
    Refined.unsafeApply(i).hashCode() ?= i.hashCode
  }

  property("unapply") = secure {
    val x: NonEmptyString = refineMV("Hi")
    val Refined(s) = x
    s ?= x.get
  }
}
