package eu.timepit.refined
package internal

import eu.timepit.refined.InferenceRule.==>
import shapeless.tag.@@

import scala.reflect.macros.blackbox

object Infer {
  def macroImpl[T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](c: blackbox.Context)(t: c.Expr[T @@ A])(ir: c.Expr[A ==> B]): c.Expr[T @@ B] = {
    import c.universe._

    val inferenceRule: A ==> B = MacroUtils.eval(c)(ir)
    if (inferenceRule.isValid)
      c.Expr(q"$t.asInstanceOf[${weakTypeOf[T @@ B]}]")
    else
      c.abort(c.enclosingPosition,
        s"invalid inference: ${weakTypeOf[A]} ==> ${weakTypeOf[B]}")
  }
}
