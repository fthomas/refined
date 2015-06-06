package eu.timepit.refined
package internal

import eu.timepit.refined.InferenceRuleAlias.==>
import shapeless.tag.@@

import scala.reflect.macros.blackbox

object Infer {
  def macroImpl[T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](c: blackbox.Context)(t: c.Expr[T @@ A])(i: c.Expr[A ==> B]): c.Expr[T @@ B] = {
    import c.universe._

    if (MacroUtils.eval(c)(i).isValid)
      c.Expr(q"$t.asInstanceOf[${weakTypeOf[T @@ B]}]")
    else {
      val aTpe = weakTypeOf[A]
      val bTpe = weakTypeOf[B]
      c.abort(c.enclosingPosition, s"invalid inference: $aTpe ==> $bTpe")
    }
  }
}
