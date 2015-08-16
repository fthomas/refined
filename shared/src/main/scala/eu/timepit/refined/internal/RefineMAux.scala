package eu.timepit.refined
package internal

import scala.reflect.macros.blackbox

/**
 * Helper class that allows the type `T` to be inferred from calls like
 * `[[RefType.refineM]][P](t)`.
 *
 * See [[http://tpolecat.github.io/2015/07/30/infer.html]] for a detailed
 * explanation of this trick.
 */
final class RefineMAux[F[_, _], P] {

  def apply[T](t: T)(implicit p: Predicate[P, T], rt: RefType[F]): F[T, P] = macro RefineMAux.macroImpl[F, T, P]
}

object RefineMAux {

  def macroImpl[F[_, _], T: c.WeakTypeTag, P: c.WeakTypeTag](c: blackbox.Context)(t: c.Expr[T])(
    p: c.Expr[Predicate[P, T]], rt: c.Expr[RefType[F]]
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
        val refType = MacroUtils.eval(c)(rt)
        refType.unsafeWrapM(c)(t)
      case Some(msg) => c.abort(c.enclosingPosition, msg)
    }
  }
}
