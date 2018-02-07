package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.predicates.all._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class CharValidateSpec extends Properties("CharValidate") {

  property("Digit.isValid") = forAll { (c: Char) =>
    isValid[Digit](c) ?= c.isDigit
  }

  property("Digit.showExpr") = secure {
    showExpr[Digit]('c') ?= "isDigit('c')"
  }

  property("Digit()") = wellTyped {
    Digit()
  }

  property("Letter.isValid") = forAll { (c: Char) =>
    isValid[Letter](c) ?= c.isLetter
  }

  property("Letter.showExpr") = secure {
    showExpr[Letter]('c') ?= "isLetter('c')"
  }

  property("LowerCase.isValid") = forAll { (c: Char) =>
    isValid[LowerCase](c) ?= c.isLower
  }

  property("LowerCase.showExpr") = secure {
    showExpr[LowerCase]('c') ?= "isLower('c')"
  }

  property("UpperCase.isValid") = forAll { (c: Char) =>
    isValid[UpperCase](c) ?= c.isUpper
  }

  property("UpperCase.showExpr") = secure {
    showExpr[UpperCase]('c') ?= "isUpper('c')"
  }
}
