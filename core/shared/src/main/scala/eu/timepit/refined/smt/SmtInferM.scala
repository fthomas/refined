package eu.timepit.refined
package smt

import eu.timepit.refined.api.RefType
import eu.timepit.refined.internal.MacroUtils

import scala.reflect.macros.Context

object SmtInferM {

  def macroImpl[F[_, _], T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](c: Context)(ta: c.Expr[F[T, A]])(
    fab: c.Expr[(Formula[A], Formula[B])], rt: c.Expr[RefType[F]]
  ): c.Expr[F[T, B]] = {
    import c.universe._
    //println("in SmtInferM")

    val (ffa, ffb) = MacroUtils.eval(c)(fab)
    //println(ffa)

    val refType = MacroUtils.eval(c)(rt)
    //MacroUtils.eval(c)(fb)
    //val ffb: Formula[B] = MacroUtils.eval(c)(fb)
    //println(ffb)
    if (ffb.subFormula("").expr.isEmpty)
      println(weakTypeOf[B])

    val imp = s"(=> ${ffa.subFormula("x").expr} ${ffb.subFormula("x").expr})"
    val fun = s"(define-fun fff () Bool $imp)"
    println(fun)

    val input = s"(declare-const x Int) $fun (assert (not fff)) (check-sat)"

    import scala.sys.process._

    val res = (s"echo $input" #| "z3 -in").!!.trim
    //println(res)

    /*
    (declare-const a Int)

    (define-fun less () Bool
      (=> (> a 10)
    (> a 0)))

    (define-fun interval () Bool
      (=> (and (> a 1) (< a 10))
    (> a 0)))

    (assert (not interval))
    (check-sat)

     */
    // use ffa and ffb to build a function for z3
    // call z3 with this function and interpret the result

    if (res == "unsat") {

      refType.unsafeRewrapM(c)(ta)
    } else {
      c.abort(c.enclosingPosition, "invalid inference")
    }
  }
}
