package eu.timepit.refined.macros

import eu.timepit.refined.api.Inference.{==>, ?=>}
import eu.timepit.refined.api.RefType
import eu.timepit.refined.internal.Resources
import macrocompat.bundle
import scala.reflect.macros.blackbox

@bundle
class InferMacro(val c: blackbox.Context) extends MacroUtils {
  import c.universe._

  def always[F[_, _], T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](ta: c.Expr[F[T, A]])(
      rt: c.Expr[RefType[F]],
      ir: c.Expr[A ==> B]
  ): c.Expr[F[T, B]] =
    refTypeInstance(rt).unsafeRewrapM(c)(ta)

  def impl[F[_, _], T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](ta: c.Expr[F[T, A]])(
      rt: c.Expr[RefType[F]],
      ir: c.Expr[A ?=> B]
  ): c.Expr[F[T, B]] = {

    val inference = eval(ir)
    if (inference.notValid) {
      abort(Resources.invalidInference(weakTypeOf[A].toString, weakTypeOf[B].toString))
    }

    refTypeInstance(rt).unsafeRewrapM(c)(ta)
  }
}
