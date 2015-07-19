package eu.timepit.refined
package internal

import shapeless.tag.@@

import scala.reflect.macros.blackbox

trait Wrapper[F[_, _]] {

  def wrap[T, P](t: T): F[T, P]

  def wrapM[T: c.WeakTypeTag, P: c.WeakTypeTag](c: blackbox.Context)(t: c.Expr[T]): c.Expr[F[T, P]]

  def rewrapM[T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](c: blackbox.Context)(ta: c.Expr[F[T, A]]): c.Expr[F[T, B]]
}

object Wrapper {

  def apply[F[_, _]](implicit w: Wrapper[F]): Wrapper[F] = w

  implicit def refinedWrapper: Wrapper[Refined] =
    new Wrapper[Refined] {
      override def wrap[T, P](t: T): Refined[T, P] =
        Refined(t)

      override def wrapM[T: c.WeakTypeTag, P: c.WeakTypeTag](c: blackbox.Context)(t: c.Expr[T]): c.Expr[Refined[T, P]] = {
        import c.universe._
        c.Expr(q"_root_.eu.timepit.refined.Refined[${weakTypeOf[T]}, ${weakTypeOf[P]}]($t)")
      }

      override def rewrapM[T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](c: blackbox.Context)(ta: c.Expr[Refined[T, A]]): c.Expr[Refined[T, B]] = {
        import c.universe._
        c.Expr(q"$ta.asInstanceOf[${weakTypeOf[Refined[T, B]]}]")
      }
    }

  implicit def tagWrapper: Wrapper[@@] =
    new Wrapper[@@] {
      override def wrap[T, P](t: T): T @@ P =
        t.asInstanceOf[T @@ P]

      override def wrapM[T: c.WeakTypeTag, P: c.WeakTypeTag](c: blackbox.Context)(t: c.Expr[T]): c.Expr[T @@ P] = {
        import c.universe._
        c.Expr(q"$t.asInstanceOf[${weakTypeOf[T @@ P]}]")
      }

      override def rewrapM[T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](c: blackbox.Context)(ta: c.Expr[T @@ A]): c.Expr[T @@ B] = {
        import c.universe._
        c.Expr(q"$ta.asInstanceOf[${weakTypeOf[T @@ B]}]")
      }
    }
}
