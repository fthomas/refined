package eu.timepit.refined

import eu.timepit.refined.boolean._
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
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

  property("Forall[LowerCase].isValid") = forAll { (s: String) =>
    Predicate[Forall[LowerCase], String].isValid(s) ?= s.forall(_.isLower)
  }

  property("Forall[LowerCase].show") = secure {
    Predicate[Forall[LowerCase], String].show("abc") ?=
      "(isLower('a') && isLower('b') && isLower('c'))"
  }

  property("Forall[UpperCase].isValid") = forAll { (s: String) =>
    Predicate[Forall[UpperCase], String].isValid(s) ?= s.forall(_.isUpper)
  }

  property("Forall[UpperCase].show") = secure {
    Predicate[Forall[UpperCase], String].show("abc") ?=
      "(isUpper('a') && isUpper('b') && isUpper('c'))"
  }

  property("Length.isValid") = forAll { (s: String) =>
    Predicate[Length[LessEqual[_10]], String].isValid(s) ?= (s.length <= 10)
  }

  property("Length.validated") = forAll { (s: String) =>
    Predicate[Length[LessEqual[_10]], String].validated(s).isEmpty ?= (s.length <= 10)
  }

  property("Length.show") = secure {
    type P = Length[Greater[_5] And LessEqual[_10]]
    Predicate[P, String].show("test") ?= "((4 > 5) && !(4 > 10))"
  }
}
