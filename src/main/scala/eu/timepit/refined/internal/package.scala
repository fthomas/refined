package eu.timepit.refined

import shapeless.tag.@@

import scala.reflect.macros.blackbox.Context

package object internal {
  def refineLitImpl[P: c.WeakTypeTag, T: c.WeakTypeTag](c: Context)(t: c.Expr[T])(p: c.Expr[Predicate[P, T]]): c.Expr[T @@ P] = {
    import c.universe._

    def predicate: Predicate[P, T] = {
      // Try evaluating p twice before failing, see
      // https://github.com/fthomas/refined/issues/3
      val expr = c.Expr[Predicate[P, T]](c.untypecheck(p.tree))
      tryTwice(c.eval(expr))
    }

    t.tree match {
      case Literal(Constant(value)) =>
        predicate.validated(value.asInstanceOf[T]) match {
          case None =>
            val pTpe = weakTypeOf[P]
            val tTpe = weakTypeOf[T]
            c.Expr(q"$t.asInstanceOf[$tTpe @@ $pTpe]")

          case Some(msg) => c.abort(c.enclosingPosition, msg)
        }

      case _ => c.abort(c.enclosingPosition, "refineLit only supports literals")
    }
  }

  private def tryTwice[T](t: => T): T =
    scala.util.Try(t).getOrElse(t)
}
