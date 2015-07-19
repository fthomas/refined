package eu.timepit.refined
package internal

import scala.reflect.macros.blackbox

/**
 * Helper class that allows the type `T` to be inferred from calls like
 * `[[refineMV]][P](t)`.
 */
final class RefineM[P, F[_, _]: Wrapper] {

  def apply[T](t: T)(implicit p: Predicate[P, T], w: Wrapper[F]): F[T, P] = macro RefineM.macroImpl[P, T, F]
}

object RefineM {

  def macroImpl[P: c.WeakTypeTag, T: c.WeakTypeTag, F[_, _]](c: blackbox.Context)(t: c.Expr[T])(
    p: c.Expr[Predicate[P, T]], w: c.Expr[Wrapper[F]]
  ): c.Expr[F[T, P]] = {
    import c.universe._

    val predicate: Predicate[P, T] = MacroUtils.eval(c)(p)
    val wrapper: Wrapper[F] = MacroUtils.eval(c)(w)

    val tValue: T = t.tree match {
      case Literal(Constant(value)) => value.asInstanceOf[T]
      case _ if predicate.isConstant => null.asInstanceOf[T]
      case _ => c.abort(c.enclosingPosition, "refineM only supports literals")
    }

    predicate.validate(tValue) match {
      case None => wrapper.wrapM(c)(t)
      case Some(msg) => c.abort(c.enclosingPosition, msg)
    }
  }
}
