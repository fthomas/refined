package eu.timepit.refined
package smt

import eu.timepit.refined.TestUtils._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class SatValidateSpec extends Properties("SatValidate") {

  type Int01 = Sat[W.`"(> x 0)"`.T]

  property("Int01.isValid") = forAll { (i: Int) =>
    isValid[Int01](i) ?= (i > 0)
  }

  property("Int01.showExpr") = secure {
    showExpr[Int01](1) ?= "(> x 0) where x = 1"
  }

  type Int02 = Sat[W.`"(and (> x 0) (< x 10))"`.T]

  property("Int02.isValid") = forAll { (i: Int) =>
    isValid[Int02](i) ?= (i > 0 && i < 10)
  }

  property("Int02.showExpr") = secure {
    showExpr[Int02](1) ?= "(and (> x 0) (< x 10)) where x = 1"
  }

  type List01 = Sat[W.`"(= (head x) 0)"`.T]

  property("List01.isValid") = forAll { (l: List[Int]) =>
    isValid[List01](l) ?= l.headOption.fold(true)(_ == 0)
  }

  property("List01.showExpr") = secure {
    showExpr[List01](List(1, 2, 3)) ?= "(= (head x) 0) where x = List(1, 2, 3)"
  }
}
