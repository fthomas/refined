package eu.timepit.refined

import _root_.cats.Contravariant
import _root_.cats.MonadError
import _root_.cats.Show
import _root_.cats.instances.eq._
import _root_.cats.instances.order._
import _root_.cats.kernel.{Eq, Order}
import eu.timepit.refined.api.{RefType, Validate}

package object cats {

  /**
   * `Eq` instance for refined types that delegates to the `Eq`
   * instance of the base type.
   */
  implicit def refTypeEq[F[_, _], T: Eq, P](implicit rt: RefType[F]): Eq[F[T, P]] =
    refTypeViaContravariant[F, Eq, T, P]

  /**
   * `Order` instance for refined types that delegates to the `Order`
   * instance of the base type.
   */
  implicit def refTypeOrder[F[_, _], T: Order, P](implicit rt: RefType[F]): Order[F[T, P]] =
    refTypeViaContravariant[F, Order, T, P]

  /**
   * `Show` instance for refined types that delegates to the `Show`
   * instance of the base type.
   */
  implicit def refTypeShow[F[_, _], T: Show, P](implicit rt: RefType[F]): Show[F[T, P]] =
    refTypeViaContravariant[F, Show, T, P]

  /**
   * `G` instance for refined types derived via `Contravariant[G]`
   * that delegates to the `G` instance of the base type.
   *
   * Typical examples for `G` are encoders.
   */
  implicit def refTypeViaContravariant[F[_, _], G[_], T, P](
      implicit c: Contravariant[G],
      rt: RefType[F],
      gt: G[T]
  ): G[F[T, P]] = c.contramap(gt)(rt.unwrap)

  /**
   * `G` instance for refined types derived via `MonadError[G, String]`
   * that is based on the `G` instance of the base type.
   *
   * Typical examples for `G` are decoders.
   */
  implicit def refTypeViaMonadError[F[_, _], G[_], T, P](
      implicit m: MonadError[G, String],
      rt: RefType[F],
      v: Validate[T, P],
      gt: G[T]
  ): G[F[T, P]] =
    m.flatMap(gt)(t => m.fromEither(rt.refine[P](t)))
}
