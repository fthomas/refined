package eu.timepit.refined

import _root_.cats.Show
import _root_.cats.implicits._
import _root_.cats.kernel.{Eq, Order}
import eu.timepit.refined.api.RefType

package object cats {

  /**
   * `Eq` instance for refined types that delegates to the `Eq`
   * instance of the base type.
   */
  implicit def refTypeEq[F[_, _], T: Eq, P](implicit rt: RefType[F]): Eq[F[T, P]] =
    Eq[T].contramap(rt.unwrap)

  /**
   * `Order` instance for refined types that delegates to the `Order`
   * instance of the base type.
   */
  implicit def refTypeOrder[F[_, _], T: Order, P](implicit rt: RefType[F]): Order[F[T, P]] =
    Order[T].contramap(rt.unwrap)

  /**
   * `Show` instance for refined types that delegates to the `Show`
   * instance of the base type.
   */
  implicit def refTypeShow[F[_, _], T: Show, P](implicit rt: RefType[F]): Show[F[T, P]] =
    Show[T].contramap(rt.unwrap)
}
