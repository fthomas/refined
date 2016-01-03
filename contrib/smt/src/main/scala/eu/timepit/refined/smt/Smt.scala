package eu.timepit.refined
package smt

import eu.timepit.refined.api.{ TypedInference, Validate }
import eu.timepit.refined.smt.smtlib._
import shapeless.Witness

case class Smt[S](s: S)

object Smt {

  implicit def smtValidate[T: Sort: Show, S <: String](implicit ws: Witness.Aux[S]): Validate[T, Smt[S]] = {
    def predicate(t: T): Boolean = {
      val script = s"""
        ${defineValue("x", t)}
        ${assert(ws.value)}
        $checkSat
      """
      unsafeInvokeZ3(script) == "sat"
    }
    Validate.fromPredicate(predicate, _ => ws.value, Smt(ws.value))
  }

  implicit def smtInference[T: Sort, A <: String, B <: String](
    implicit
    wa: Witness.Aux[A],
    wb: Witness.Aux[B]
  ): TypedInference[T, Smt[A], Smt[B]] = {
    val isValid = {
      val script = s"""
        ${declareConst[T]("x")}
        ${defineInference(wa.value, wb.value)}
        $assertInference
        $checkSat
      """
      unsafeInvokeZ3(script) == "unsat"
    }
    TypedInference(isValid, s"smtInference(${wa.value}, ${wb.value})")
  }
}
