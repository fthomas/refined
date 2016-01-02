package eu.timepit.refined
package smt

import eu.timepit.refined.api.{ TypedInference, Validate }
import eu.timepit.refined.smt.smtlib._
import shapeless.Witness

case class SmtEval[S](s: S)

object SmtEval {

  implicit def smtEvalValidate[T: Sort, S <: String](implicit ws: Witness.Aux[S]): Validate[T, SmtEval[S]] = {
    def predicate(t: T): Boolean = {
      val script = s"""
        ${defineValue("x", t)}
        ${assert(ws.value)}
        $checkSat
      """
      unsafeInvokeZ3(script) == "sat"
    }
    Validate.fromPredicate(predicate, _ => ws.value, SmtEval(ws.value))
  }

  implicit def smtEvalInference[T: Sort, A <: String, B <: String](
    implicit
    wa: Witness.Aux[A],
    wb: Witness.Aux[B]
  ): TypedInference[T, SmtEval[A], SmtEval[B]] = {
    val isValid = {
      val script = s"""
        ${declareConst("x")}
        ${defineInference(wa.value, wb.value)}
        $assertInference
        $checkSat
      """
      unsafeInvokeZ3(script) == "unsat"
    }
    TypedInference(isValid, s"smtEvalInference(${wa.value}, ${wb.value})")
  }
}
