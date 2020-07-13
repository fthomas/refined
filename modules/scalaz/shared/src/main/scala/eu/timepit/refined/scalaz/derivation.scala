// Copyright: 2015 - 2018 Frank S. Thomas and Sam Halliday
// License: https://opensource.org/licenses/MIT

package eu.timepit.refined.scalaz

import eu.timepit.refined.api.{RefType, Validate}
import scalaz.{Contravariant, MonadError}

object derivation extends DerivationInstances

trait DerivationInstances {

  /**
   * Instances for typeclasses with a `Contravariant`, e.g. encoders.
   */
  implicit def refTypeViaContravariant[F[_, _], G[_], T, P](implicit
      c: Contravariant[G],
      rt: RefType[F],
      gt: G[T]
  ): G[F[T, P]] = c.contramap(gt)(rt.unwrap)

  /**
   * Instances for typeclasses with a `MonadError[?, String]`, i.e. a
   * disjunction kleisli arrow applied to the typeclass. e.g. decoders.
   */
  implicit def refTypeViaMonadError[F[_, _], G[_], T, P](implicit
      m: MonadError[G, String],
      rt: RefType[F],
      v: Validate[T, P],
      gt: G[T]
  ): G[F[T, P]] =
    m.bind(gt) { f =>
      rt.refine(f) match {
        case Left(s)    => m.raiseError(s)
        case Right(ftp) => m.pure(ftp)
      }
    }
}
