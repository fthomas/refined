package eu.timepit.refined
package smt

import eu.timepit.refined.api.Validate
import eu.timepit.refined.smt.smtlib.DefineFun
import shapeless.Witness

import scala.sys.process._

case class SmtEval[S](s: S)

object SmtEval {

  implicit def smtEvalValidate[T: DefineFun, S <: String](implicit ws: Witness.Aux[S]): Validate[T, SmtEval[S]] = {

    def p(t: T): Boolean = {
      val function = DefineFun[T].asString(t)
      val assertion = s"(assert ${ws.value})"
      val cmdline = s"$function $assertion (check-sat)"
      val sat = (s"echo $cmdline" #| "z3 -in").!!.trim
      sat == "sat"
    }

    Validate.fromPredicate(p, _ => ws.value, SmtEval(ws.value))
  }
}
