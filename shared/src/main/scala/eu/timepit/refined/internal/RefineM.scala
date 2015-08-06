package eu.timepit.refined
package internal

import scala.reflect.macros.Context

/**
 * Helper class that allows the type `T` to be inferred from calls like
 * `[[refineMV]][P](t)`.
 */
final class RefineM[P, F[_, _]] {

  def apply[T](t: T)(implicit p: Predicate[P, T], w: Wrapper[F]): F[T, P] = macro RefineM.macroImpl[P, T, F]
}

object RefineM {

  def macroImpl[P: c.WeakTypeTag, T: c.WeakTypeTag, F[_, _]](c: Context)(t: c.Expr[T])(
    p: c.Expr[Predicate[P, T]], w: c.Expr[Wrapper[F]]
  ): c.Expr[F[T, P]] = {
    import c.universe._

    val predicate = MacroUtils.eval(c)(p)

    val tValue: T = t.tree match {
      case Literal(Constant(value)) => value.asInstanceOf[T]
      case _ if predicate.isConstant => null.asInstanceOf[T]
      case _ => c.abort(
        c.enclosingPosition,
        "compile-time refinement only works with literals or constant predicates"
      )
    }

    predicate.validate(tValue) match {
      case None =>
        val wrapper = MacroUtils.eval(c)(w)
        wrapper.wrapM(c)(t)
      case Some(msg) => c.abort(c.enclosingPosition, msg)
    }
  }
}
