package eu.timepit.refined
package internal

import shapeless.tag.@@

import scala.reflect.macros.blackbox

object Infer {
  def macroImpl[T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](c: blackbox.Context)(t: c.Expr[T @@ A])(i: c.Expr[Inference[A, B]]): c.Expr[T @@ B] = {
    import c.universe._

    val tTpe = weakTypeOf[T]
    val aTpe = weakTypeOf[A]
    val bTpe = weakTypeOf[B]

    if (MacroUtils.eval(c)(i).isValid)
      c.Expr(q"$t.asInstanceOf[$tTpe @@ $bTpe]")
    else
      c.abort(c.enclosingPosition, s"invalid inference: $aTpe -> $bTpe")
  }
}
