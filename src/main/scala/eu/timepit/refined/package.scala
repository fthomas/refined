package eu.timepit

import eu.timepit.refined.internal.{ Refine, RefineLit }
import shapeless.tag.@@

import scala.language.implicitConversions
import scala.reflect.macros.blackbox

package object refined {
  val W = shapeless.Witness

  def refine[P]: Refine[P] = new Refine[P]
  def refineLit[P]: RefineLit[P] = new RefineLit[P]

  implicit def infer[T, A, B](t: T @@ A)(implicit i: Inference[A, B]): T @@ B = macro inferMacro[T, A, B]
  //if (i.isValid) t.asInstanceOf[T @@ B] else sys.error("invalid inference")

  def inferMacro[T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](c: blackbox.Context)(t: c.Expr[T @@ A])(i: c.Expr[Inference[A, B]]): c.Expr[T @@ B] = {
    import c.universe._

    def tryTwice[G](t: => G): G =
      scala.util.Try(t).getOrElse(t)

    def inference: Inference[A, B] = {
      val expr = c.Expr[Inference[A, B]](c.untypecheck(i.tree))
      tryTwice(c.eval(expr))
    }

    if (inference.isValid)
      c.Expr(q"$t.asInstanceOf[${weakTypeOf[T]} @@ ${weakTypeOf[B]}]")
    else
      c.abort(c.enclosingPosition, "invalid inference")
  }
}
