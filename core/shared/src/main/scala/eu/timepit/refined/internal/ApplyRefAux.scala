package eu.timepit.refined
package internal

import eu.timepit.refined.api.{ RefType, Validate }

/**
 * Helper class that allows the types `F`, `T`, and `P` to be inferred
 * from calls like `[[api.RefType.applyRef]][F[T, P]](t)`.
 *
 * See [[http://tpolecat.github.io/2015/07/30/infer.html]] for a detailed
 * explanation of this trick.
 */
final class ApplyRefAux[FTP] {

  def apply[F[_, _], T, P](t: T)(
    implicit
    ev: FTP =:= F[T, P], rt: RefType[F], v: Validate[T, P]
  ): Either[String, F[T, P]] =
    rt.refine[P](t)
}
