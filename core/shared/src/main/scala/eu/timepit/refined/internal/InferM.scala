package eu.timepit.refined
package internal

import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.api.RefType
import macrocompat.bundle

import scala.reflect.macros.blackbox

@bundle
class InferM(val c: blackbox.Context) extends MacroUtils {
  import c.universe._

  def macroImpl[F[_, _], T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](ta: c.Expr[F[T, A]])(
    ir: c.Expr[A ==> B], rt: c.Expr[RefType[F]]
  ): c.Expr[F[T, B]] = {

    val inferenceRule = eval(ir)
    if (inferenceRule.notValid) {
      abort(s"invalid inference: ${weakTypeOf[A]} ==> ${weakTypeOf[B]}")
    }

    val refType = eval(rt)
    refType.unsafeRewrapM(c)(ta)
  }
}
