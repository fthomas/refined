package eu.timepit.refined
package smt

import eu.timepit.refined.api.{ TypedInference, Validate }
import eu.timepit.refined.smt.smtlib._
import shapeless.Witness

case class Sat[S](s: S)

object Sat {

  implicit def satValidate[T: Sort: Show, S <: String](
    implicit
    ws: Witness.Aux[S]
  ): Validate[T, Sat[S]] = {
    def predicate(t: T): Boolean = {
      val script = s"""
        |${defineValue("x", t)}
        |${assert(ws.value)}
        |$checkSat
      """.stripMargin.trim
      unsafeInvokeZ3(script) == sat
    }
    Validate.fromPredicate(predicate, x => s"${ws.value} where x = $x", Sat(ws.value))
  }

  implicit def satInference[T: Sort, A <: String, B <: String](
    implicit
    wa: Witness.Aux[A],
    wb: Witness.Aux[B]
  ): TypedInference[T, Sat[A], Sat[B]] = {
    val isValid = {
      val script = s"""
        |${declareConst[T]("x")}
        |${defineInference(wa.value, wb.value)}
        |$assertInference
        |$checkSat
      """.stripMargin.trim
      unsafeInvokeZ3(script) == unsat
    }
    TypedInference(isValid, s"satInference(${wa.value}, ${wb.value})")
  }
}
