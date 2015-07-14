package eu.timepit.refined
package internal

import scala.reflect.macros.blackbox

/**
 * Helper class that allows the type `T` to be inferred from calls like
 * `[[refineM]][P](t)`.
 */
final class RefineM[P] {
  def apply[T](t: T)(implicit p: Predicate[P, T]): Refined[T, P] = macro RefineM.macroImpl[P, T]
}

object RefineM {
  def macroImpl[P: c.WeakTypeTag, T: c.WeakTypeTag](c: blackbox.Context)(t: c.Expr[T])(p: c.Expr[Predicate[P, T]]): c.Expr[Refined[T, P]] = {
    import c.universe._

    val litValue: T = t.tree match {
      case Literal(Constant(value)) => value.asInstanceOf[T]
      case _ => c.abort(c.enclosingPosition, "refineM only supports literals")
    }

    val predicate: Predicate[P, T] = MacroUtils.eval(c)(p)
    predicate.validate(litValue) match {
      case None =>
        c.Expr(q"_root_.eu.timepit.refined.Refined[${weakTypeOf[T]}, ${weakTypeOf[P]}]($t)")
      case Some(msg) => c.abort(c.enclosingPosition, msg)
    }
  }
}
