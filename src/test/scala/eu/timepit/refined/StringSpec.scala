package eu.timepit.refined

import eu.timepit.refined.generic._
import eu.timepit.refined.numeric.LessEqual
import eu.timepit.refined.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class StringSpec extends Properties("string") {
  property("Empty") = forAll { (s: String) =>
    Predicate[Empty, String].isValid(s) == s.isEmpty
  }

  property("NonEmpty") = forAll { (s: String) =>
    Predicate[NonEmpty, String].isValid(s) == s.nonEmpty
  }

  property("LowerCase") = forAll { (s: String) =>
    Predicate[LowerCase, String].isValid(s) == s.forall(_.isLower)
  }

  property("UpperCase") = forAll { (s: String) =>
    Predicate[UpperCase, String].isValid(s) == s.forall(_.isUpper)
  }

  property("Length") = forAll { (s: String) =>
    Predicate[Length[LessEqual[_10]], String].isValid(s) == (s.length <= 10)
  }
}
