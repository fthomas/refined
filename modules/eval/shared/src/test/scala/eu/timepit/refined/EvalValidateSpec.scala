package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.Validate
import eu.timepit.refined.eval.Eval
import org.scalacheck.Prop._
import org.scalacheck.Properties
import scala.tools.reflect.ToolBoxError
import shapeless.test.illTyped

class EvalValidateSpec extends Properties("EvalValidate") {

  type IsEven = Eval[W.`"(x: Int) => x % 2 == 0"`.T]

  property("Eval.isValid") = {
    val v = Validate[Int, IsEven]
    forAll((i: Int) => v.isValid(i) ?= (i % 2 == 0))
  }

  property("Eval.showExpr") = secure {
    Validate[Int, IsEven].showExpr(0) ?= "(x: Int) => x % 2 == 0"
  }

  property("Eval.refineMV") = wellTyped {
    refineMV[IsEven](2)
    illTyped("refineMV[IsEven](3)", "Predicate.*fail.*")
  }

  property("Eval.refineV.no parameter type") = {
    val v = Validate[List[Int], Eval[W.`"_.headOption.fold(false)(_ > 0)"`.T]]
    forAll((l: List[Int]) => v.isValid(l) ?= l.headOption.fold(false)(_ > 0))
  }

  property("Eval.refineMV.scope") = wellTyped {
    val two = 2
    illTyped(
      """refineMV[Eval[W.`"(x: Int) => x >= two"`.T]](2)""",
      "exception during macro expansion.*"
    )
  }

  property("Eval.refineV.scope") = secure {
    val two = 2
    throws(classOf[ToolBoxError])(refineV[Eval[W.`"(x: Int) => x >= two"`.T]](two))
  }
}
