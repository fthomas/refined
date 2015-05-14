package eu.timepit.refined

import eu.timepit.refined.char._
import eu.timepit.refined.generic._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class CharSpec extends Properties("char") {
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
