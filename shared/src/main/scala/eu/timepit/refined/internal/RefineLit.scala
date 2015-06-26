package eu.timepit.refined
package internal

import shapeless.tag.@@

import scala.reflect.macros.blackbox

final class RefineLit[P] {
  def apply[T](t: T)(implicit p: Predicate[P, T]): T @@ P = macro RefineLit.macroImpl[P, T]
}

object RefineLit {
  def macroImpl[P: c.WeakTypeTag, T: c.WeakTypeTag](c: blackbox.Context)(t: c.Expr[T])(p: c.Expr[Predicate[P, T]]): c.Expr[T @@ P] = {
    import c.universe._

    def predicate: Predicate[P, T] = MacroUtils.eval(c)(p)

    t.tree match {
      case Literal(Constant(value)) =>
        predicate.validated(value.asInstanceOf[T]) match {
          case None => c.Expr(q"$t.asInstanceOf[${weakTypeOf[T @@ P]}]")
          case Some(msg) => c.abort(c.enclosingPosition, msg)
        }

      case _ => c.abort(c.enclosingPosition, "refineLit only supports literals")
    }
  }
}
