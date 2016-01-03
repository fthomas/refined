package eu.timepit.refined
package smt

import eu.timepit.refined.TestUtils._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class SmtEvalValidateSpec extends Properties("SmtEvalValidate") {

  type Int01 = Smt[W.`"(> x 0)"`.T]

  property("Int01.isValid") = forAll { (i: Int) =>
    isValid[Int01](i) ?= (i > 0)
  }

  property("Int01.showExpr") = secure {
    showExpr[Int01](1) ?= "(> x 0)"
  }

  type Int02 = Smt[W.`"(and (> x 0) (< x 10))"`.T]

  property("Int02.isValid") = forAll { (i: Int) =>
    isValid[Int02](i) ?= (i > 0 && i < 10)
  }

  property("Int02.showExpr") = secure {
    showExpr[Int02](1) ?= "(and (> x 0) (< x 10))"
  }
}
