package eu.timepit.refined

import eu.timepit.refined.boolean._
import eu.timepit.refined.generic._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class StringSpec extends Properties("string") {
  property("Empty.isValid") = forAll { (s: String) =>
    Predicate[Empty, String].isValid(s) ?= s.isEmpty
  }

  property("Empty.show") = secure {
    Predicate[Empty, String].show("test") ?= "isEmpty(test)"
  }

  property("NonEmpty.isValid") = forAll { (s: String) =>
    Predicate[NonEmpty, String].isValid(s) ?= s.nonEmpty
  }

  property("NonEmpty.show") = secure {
    Predicate[NonEmpty, String].show("test") ?= "!isEmpty(test)"
  }

  property("LowerCase.isValid") = forAll { (s: String) =>
    Predicate[LowerCase, String].isValid(s) ?= s.forall(_.isLower)
  }

  property("LowerCase.show") = secure {
    Predicate[LowerCase, String].show("test") ?= "isLowerCase(test)"
  }

  property("UpperCase.isValid") = forAll { (s: String) =>
    Predicate[UpperCase, String].isValid(s) ?= s.forall(_.isUpper)
  }

  property("UpperCase.show") = secure {
    Predicate[UpperCase, String].show("test") ?= "isUpperCase(test)"
  }

  property("Length.isValid") = forAll { (s: String) =>
    Predicate[Length[LessEqual[_10]], String].isValid(s) ?= (s.length <= 10)
  }

  property("Length.show") = secure {
    type P = Length[Greater[_5] And LessEqual[_10]]
    Predicate[P, String].show("test") ?= "((4 > 5) && !(4 > 10))"
  }
}
