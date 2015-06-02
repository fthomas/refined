package eu.timepit.refined

import eu.timepit.refined.char._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class CharPredicateSpec extends Properties("CharPredicate") {

  property("Digit.isValid") = forAll { (c: Char) =>
    Predicate[Digit, Char].isValid(c) ?= c.isDigit
  }

  property("Digit.show") = secure {
    Predicate[Digit, Char].show('c') ?= "isDigit('c')"
  }

  property("LowerCase.isValid") = forAll { (c: Char) =>
    Predicate[LowerCase, Char].isValid(c) ?= c.isLower
  }

  property("LowerCase.show") = secure {
    Predicate[LowerCase, Char].show('c') ?= "isLower('c')"
  }

  property("UpperCase.isValid") = forAll { (c: Char) =>
    Predicate[UpperCase, Char].isValid(c) ?= c.isUpper
  }

  property("UpperCase.show") = secure {
    Predicate[UpperCase, Char].show('c') ?= "isUpper('c')"
  }
}
