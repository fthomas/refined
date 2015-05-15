package eu.timepit.refined

import shapeless.tag.@@

import scala.reflect.macros.blackbox.Context

package object internal {
  def refineLitImpl[P: c.WeakTypeTag, T: c.WeakTypeTag](c: Context)(t: c.Expr[T])(p: c.Expr[Predicate[P, T]]): c.Expr[T @@ P] = {
    import c.universe._

    t.tree match {
      case Literal(Constant(value)) =>
        val predicate: Predicate[P, T] = c.eval(c.Expr(c.untypecheck(p.tree)))

        predicate.validated(value.asInstanceOf[T]) match {
          case None => c.Expr(q"shapeless.tag[${weakTypeOf[P]}]($t)")
          case Some(msg) => c.abort(c.enclosingPosition, msg)
        }

      case _ => c.abort(c.enclosingPosition, "refineLit only supports literals")
    }
  }
}
