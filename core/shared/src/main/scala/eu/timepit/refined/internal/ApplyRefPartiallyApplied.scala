package eu.timepit.refined
package internal

import eu.timepit.refined.api.RefinedType

/**
 * Helper class that allows the type `T` to be inferred from calls like
 * `[[api.RefType.applyRef]][F[T, P]](t)`.
 *
 * See [[http://tpolecat.github.io/2015/07/30/infer.html]] for a detailed
 * explanation of this trick.
 */
final class ApplyRefPartiallyApplied[FTP] {

  def apply[T](t: T)(implicit rt: RefinedType.AuxT[FTP, T]): Either[String, FTP] =
    rt.refine(t)

  def unsafeFrom[T](t: T)(implicit rt: RefinedType.AuxT[FTP, T]): FTP =
    rt.refineUnsafe(t)
}
