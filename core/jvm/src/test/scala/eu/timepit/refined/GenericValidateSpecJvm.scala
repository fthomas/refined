package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.generic.Eval
import org.scalacheck.Prop._
import org.scalacheck.Properties

class GenericValidateSpecJvm extends Properties("GenericValidate") {

  property("Eval.isValid") = secure {
    isValid[Eval[W.`"(x: Int) => x % 2 == 0"`.T]](6) &&
      notValid[Eval[W.`"(x: Int) => x % 2 == 0"`.T]](7)
  }

  property("Eval.showExpr") = secure {
    showExpr[Eval[W.`"(x: Int) => x % 2 == 0"`.T]](0) ?= "(x: Int) => x % 2 == 0"
  }
}
