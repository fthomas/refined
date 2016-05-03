package eu.timepit.refined

import _root_.scalaz.@@
import eu.timepit.refined.api.RefType
import scala.reflect.macros.blackbox

package object scalaz {

  implicit val scalazTagRefType: RefType[@@] = new RefType[@@] {
    override def unsafeWrap[T, P](t: T): T @@ P = t.asInstanceOf[T @@ P]

    override def unwrap[T, P](tp: T @@ P): T = tp.asInstanceOf[T]

    override def unsafeRewrap[T, A, B](ta: T @@ A): T @@ B =
      ta.asInstanceOf[T @@ B]

    override def unsafeWrapM[T : c.WeakTypeTag, P : c.WeakTypeTag](c: blackbox.Context)(
        t: c.Expr[T]): c.Expr[T @@ P] =
      c.universe.reify(t.splice.asInstanceOf[T @@ P])

    override def unsafeRewrapM[T : c.WeakTypeTag, A : c.WeakTypeTag, B : c.WeakTypeTag](
        c: blackbox.Context)(ta: c.Expr[T @@ A]): c.Expr[T @@ B] =
      c.universe.reify(ta.splice.asInstanceOf[T @@ B])
  }
}
