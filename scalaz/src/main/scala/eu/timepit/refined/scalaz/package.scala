package eu.timepit.refined

import eu.timepit.refined.api.RefType

import _root_.scalaz.{ @@, Tag }
import scala.reflect.macros.Context

package object scalaz {

  implicit val scalazTagRefType: RefType[@@] =
    new RefType[@@] {
      override def unsafeWrap[T, P](t: T): T @@ P =
        Tag.apply(t)

      override def unwrap[T, P](tp: T @@ P): T =
        Tag.unwrap(tp)

      override def unsafeWrapM[T: c.WeakTypeTag, P: c.WeakTypeTag](c: Context)(t: c.Expr[T]): c.Expr[T @@ P] =
        c.universe.reify(Tag.apply[T, P](t.splice))

      override def unsafeRewrapM[T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](c: Context)(ta: c.Expr[T @@ A]): c.Expr[T @@ B] =
        c.universe.reify(Tag.apply[T, B](Tag.unwrap(ta.splice)))
    }
}
