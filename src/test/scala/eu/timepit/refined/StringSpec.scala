package eu.timepit.refined

import eu.timepit.refined.generic._
import eu.timepit.refined.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class StringSpec extends Properties("string") {
  property("NonEmpty") = forAll { (s: String) =>
    implicitly[Predicate[NonEmpty, String]].isValid(s) == s.nonEmpty
  }

  property("Empty") = forAll { (s: String) =>
    implicitly[Predicate[Empty, String]].isValid(s) == s.isEmpty
  }

  property("LowerCase") = forAll { (s: String) =>
    implicitly[Predicate[LowerCase, String]].isValid(s) == s.forall(_.isLower)
  }
}
