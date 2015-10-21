package eu.timepit.refined
package internal

import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.api.RefType

import scala.reflect.macros.blackbox

object InferM {

  def macroImpl[F[_, _], T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](c: blackbox.Context)(ta: c.Expr[F[T, A]])(
    ir: c.Expr[A ==> B], rt: c.Expr[RefType[F]]
  ): c.Expr[F[T, B]] = {
    import c.universe._

    val inferenceRule = MacroUtils.eval(c)(ir)
    if (inferenceRule.notValid) {
      c.abort(c.enclosingPosition, s"invalid inference: ${weakTypeOf[A]} ==> ${weakTypeOf[B]}")
    }

    val refType = MacroUtils.eval(c)(rt)
    refType.unsafeRewrapM(c)(ta)
  }
}
