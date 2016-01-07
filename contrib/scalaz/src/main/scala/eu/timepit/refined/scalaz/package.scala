package eu.timepit.refined

import _root_.scalaz.@@
import eu.timepit.refined.api.RefType

package object scalaz {

  implicit val scalazTagRefType: RefType[@@] =
    new RefType[@@] {
      override def unsafeWrap[T, P](t: T): T @@ P =
        t.asInstanceOf[T @@ P]

      override def unwrap[T, P](tp: T @@ P): T =
        tp.asInstanceOf[T]

      override def unsafeRewrap[T, A, B](ta: T @@ A): T @@ B =
        ta.asInstanceOf[T @@ B]
    }
}
