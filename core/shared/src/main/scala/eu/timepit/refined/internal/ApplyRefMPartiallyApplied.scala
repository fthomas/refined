package eu.timepit.refined
package internal

import eu.timepit.refined.api.RefinedType

/**
 * Helper class that allows the types `F`, `T`, and `P` to be inferred
 * from calls like `[[api.RefType.applyRefM]][F[T, P]](t)`.
 *
 * See [[http://tpolecat.github.io/2015/07/30/infer.html]] for a detailed
 * explanation of this trick.
 */
final class ApplyRefMPartiallyApplied[FTP] {

  def apply[T](t: T)(
    implicit
    rt: RefinedType.AuxT[FTP, T]
  ): FTP = macro macros.RefineMacro.refineImpl[FTP, T, rt.P]
}
