package eu.timepit.refined
package smt

import eu.timepit.refined.api.{ TypedInference, Validate }
import eu.timepit.refined.smt.smtlib._
import shapeless.Witness

case class SmtEval[S](s: S)

object SmtEval {

  implicit def smtEvalValidate[T: Sort, S <: String](implicit ws: Witness.Aux[S]): Validate[T, SmtEval[S]] = {

    def p(t: T): Boolean = {
      val function = defineValue("x", t)
      val assertion = assert(ws.value)
      val script = s"$function $assertion $checkSat"
      val sat = invokeZ3(script)
      sat == "sat"
    }

    Validate.fromPredicate(p, _ => ws.value, SmtEval(ws.value))
  }

  implicit def smtEvalInference[T: Sort, A <: String, B <: String](
    implicit
    wa: Witness.Aux[A],
    wb: Witness.Aux[B]
  ): TypedInference[T, SmtEval[A], SmtEval[B]] = {
    def check: Boolean = {
      val const = declareConst("x")
      val inf = defineInference(wa.value, wb.value)
      val script = s"$const $inf (assert (not inference)) $checkSat"
      val sat = invokeZ3(script)
      sat == "unsat"
    }
    TypedInference(check, s"smtEvalInference(${wa.value}, ${wb.value})")
  }
}
