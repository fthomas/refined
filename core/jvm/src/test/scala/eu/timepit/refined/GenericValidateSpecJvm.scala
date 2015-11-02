package eu.timepit.refined

import eu.timepit.refined.api.Validate
import eu.timepit.refined.generic.Eval
import org.scalacheck.Prop._
import org.scalacheck.Properties

class GenericValidateSpecJvm extends Properties("GenericValidate") {

  val even = Validate[Int, Eval[W.`"(x: Int) => x % 2 == 0"`.T]]

  property("Eval.isValid") = forAll { (i: Int) =>
    even.isValid(i) ?= (i % 2 == 0)
  }

  property("Eval.showExpr") = secure {
    even.showExpr(0) ?= "(x: Int) => x % 2 == 0"
  }
}
