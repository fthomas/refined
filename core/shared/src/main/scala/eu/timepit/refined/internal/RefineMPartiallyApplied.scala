package eu.timepit.refined
package internal

import eu.timepit.refined.api.{ RefType, Validate }

import scala.reflect.macros.Context

/**
 * Helper class that allows the type `T` to be inferred from calls like
 * `[[api.RefType.refineM]][P](t)`.
 *
 * See [[http://tpolecat.github.io/2015/07/30/infer.html]] for a detailed
 * explanation of this trick.
 */
final class RefineMPartiallyApplied[F[_, _], P] {

  def apply[T](t: T)(implicit v: Validate[T, P], rt: RefType[F]): F[T, P] = macro RefineMPartiallyApplied.macroImpl[F, T, P]
}

object RefineMPartiallyApplied {

  def macroImpl[F[_, _], T: c.WeakTypeTag, P: c.WeakTypeTag](c: Context)(t: c.Expr[T])(
    v: c.Expr[Validate[T, P]], rt: c.Expr[RefType[F]]
  ): c.Expr[F[T, P]] = {
    import c.universe._

    val validate = MacroUtils.eval(c)(v)

    val tValue: T = t.tree match {
      case Literal(Constant(value)) => value.asInstanceOf[T]
      case _ if validate.isConstant => null.asInstanceOf[T]
      case _ =>
        val msg = "compile-time refinement only works with literals or constant predicates"
        c.abort(c.enclosingPosition, msg)
    }

    val res = validate.validate(tValue)
    if (res.isFailed) {
      c.abort(c.enclosingPosition, validate.showResult(tValue, res))
    }

    val refType = MacroUtils.eval(c)(rt)
    refType.unsafeWrapM(c)(t)
  }
}
