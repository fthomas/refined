package eu.timepit.refined

import _root_.cats.{Contravariant, MonadError, Show}
import _root_.cats.implicits._
import _root_.cats.kernel.{Eq, Order}
import eu.timepit.refined.api.{RefType, Validate}

package object cats {

  /**
   * `Eq` instance for refined types that delegates to the `Eq`
   * instance of the base type.
   */
  implicit def refTypeEq[F[_, _], T: Eq, P](implicit rt: RefType[F]): Eq[F[T, P]] =
    cats.derivation.refTypeViaContravariant[F, Eq, T, P]

  /**
   * `Order` instance for refined types that delegates to the `Order`
   * instance of the base type.
   */
  implicit def refTypeOrder[F[_, _], T: Order, P](implicit rt: RefType[F]): Order[F[T, P]] =
    cats.derivation.refTypeViaContravariant[F, Order, T, P]

  /**
   * `Show` instance for refined types that delegates to the `Show`
   * instance of the base type.
   */
  implicit def refTypeShow[F[_, _], T: Show, P](implicit rt: RefType[F]): Show[F[T, P]] =
    cats.derivation.refTypeViaContravariant[F, Show, T, P]

  @deprecated("Generic instances have been moved into the `generic` object", "0.9.4")
  def refTypeViaContravariant[F[_, _], G[_], T, P](
      implicit c: Contravariant[G],
      rt: RefType[F],
      gt: G[T]
  ): G[F[T, P]] =
    cats.derivation.refTypeViaContravariant[F, G, T, P]

  @deprecated("Generic instances have been moved into the `generic` object", "0.9.4")
  def refTypeViaMonadError[F[_, _], G[_], T, P](
      implicit m: MonadError[G, String],
      rt: RefType[F],
      v: Validate[T, P],
      gt: G[T]
  ): G[F[T, P]] =
    cats.derivation.refTypeViaMonadError[F, G, T, P]
}
