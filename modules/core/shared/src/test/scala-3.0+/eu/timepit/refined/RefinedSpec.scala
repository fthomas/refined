package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.types.string.NonEmptyString
import org.scalacheck.Prop._
import org.scalacheck.Properties
import eu.timepit.refined.test.ScalaVersionSpecific.illTyped

class RefinedSpec extends Properties("Refined") {

  property("apply") = wellTyped {
    illTyped(
      """ val x: NonEmptyString = Refined("") """,
      "does not take parameters"
    )
  }

  property("copy") = wellTyped {
    // Self-contained snippet: `typeCheckErrors` does not see block-local vals, so inline the value.
    illTyped(
      """ refineMV[NonEmpty]("abc").copy("") """,
      "copy is not a member"
    )
  }

  property("equals") = secure {
    // Note: unlike the Scala 2 value class, `Refined` is an opaque type on Scala 3 and erases to its
    // base type, so `Refined.unsafeApply(1).equals(1)` is `true` — only reflexive equality is checked.
    Refined.unsafeApply(1) ?= Refined.unsafeApply(1)
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
