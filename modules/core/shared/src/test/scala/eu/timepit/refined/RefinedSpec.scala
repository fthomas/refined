package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.types.string.NonEmptyString
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.test.illTyped

class RefinedSpec extends Properties("Refined") {

  property("apply") = wellTyped {
    illTyped(
      """ val x: NonEmptyString = Refined("") """,
      "eu.timepit.refined.api.Refined.type does not take parameters"
    )
  }

  property("copy") = wellTyped {
    val x: NonEmptyString = refineMV[NonEmpty]("abc")
    illTyped(
      """ x.copy("") """,
      "value copy is not a member of eu.timepit.refined.types.string.NonEmptyString"
    )
  }

  property("equals") = secure {
    (Refined.unsafeApply(1) ?= Refined.unsafeApply(1)) &&
    !Refined.unsafeApply(1).equals(1)
  }

  property("hashCode") = forAll((i: Int) => Refined.unsafeApply(i).hashCode() ?= i.hashCode)

  property("unapply") = secure {
    val x: NonEmptyString = refineMV("Hi")
    val Refined(s) = x
    s ?= x.value
  }

  property("unapply in pattern matching") = secure {
    val x: NonEmptyString = refineMV("abc")
    x match {
      case Refined("abc") => true
      case _              => false
    }
  }
}
