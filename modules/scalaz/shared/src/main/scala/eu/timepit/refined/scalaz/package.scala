package eu.timepit.refined

import _root_.scalaz.@@
import _root_.scalaz.Equal
import _root_.scalaz.Show
import _root_.scalaz.syntax.contravariant._
import eu.timepit.refined.api.RefType
import scala.reflect.macros.blackbox

package object scalaz {

  implicit val scalazTagRefType: RefType[@@] =
    new RefType[@@] {
      override def unsafeWrap[T, P](t: T): T @@ P =
        t.asInstanceOf[T @@ P]

      override def unwrap[T](tp: T @@ _): T =
        tp.asInstanceOf[T]

      override def unsafeRewrap[T, A, B](ta: T @@ A): T @@ B =
        ta.asInstanceOf[T @@ B]

      override def unsafeWrapM[T: c.WeakTypeTag, P: c.WeakTypeTag](c: blackbox.Context)(
          t: c.Expr[T]): c.Expr[T @@ P] =
        c.universe.reify(t.splice.asInstanceOf[T @@ P])

      override def unsafeRewrapM[T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](
          c: blackbox.Context)(ta: c.Expr[T @@ A]): c.Expr[T @@ B] =
        c.universe.reify(ta.splice.asInstanceOf[T @@ B])
    }

  /**
   * `Equal` instance for refined types that delegates to the `Equal`
   * instance of the base type.
   */
  implicit def refTypeEqual[F[_, _], T: Equal, P](implicit rt: RefType[F]): Equal[F[T, P]] =
    Equal[T].contramap(rt.unwrap)

  /**
   * `Show` instance for refined types that delegates to the `Show`
   * instance of the base type.
   */
  implicit def refTypeShow[F[_, _], T: Show, P](implicit rt: RefType[F]): Show[F[T, P]] =
    Show[T].contramap(rt.unwrap)
}
