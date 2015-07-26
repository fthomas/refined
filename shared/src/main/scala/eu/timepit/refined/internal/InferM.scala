package eu.timepit.refined
package internal

import eu.timepit.refined.InferenceRule.==>

import scala.reflect.macros.blackbox

object InferM {

  def macroImpl[T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, F[_, _]](c: blackbox.Context)(t: c.Expr[F[T, A]])(
    ir: c.Expr[A ==> B], w: c.Expr[Wrapper[F]]
  ): c.Expr[F[T, B]] = {
    import c.universe._

    val inferenceRule = MacroUtils.eval(c)(ir)

    if (inferenceRule.isValid) {
      val wrapper = MacroUtils.eval(c)(w)
      wrapper.rewrapM(c)(t)
    }
    else
      c.abort(c.enclosingPosition, s"invalid inference: ${weakTypeOf[A]} ==> ${weakTypeOf[B]}")
  }
}
