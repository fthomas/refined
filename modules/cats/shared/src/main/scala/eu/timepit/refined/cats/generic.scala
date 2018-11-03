package eu.timepit.refined.cats

import cats.{Contravariant, MonadError}
import eu.timepit.refined.api.{RefType, Validate}

object generic {

  /**
   * `G` instance for refined types derived via `Contravariant[G]`
   * that delegates to the `G` instance of the base type.
   *
   * Typical examples for `G` are encoders.
   */
  implicit def refTypeViaContravariant[F[_, _], G[_], T, P](
      implicit
      c: Contravariant[G],
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
      implicit
      m: MonadError[G, String],
      rt: RefType[F],
      v: Validate[T, P],
      gt: G[T]
  ): G[F[T, P]] =
    m.flatMap(gt)(t => m.fromEither(rt.refine[P](t)))
}
