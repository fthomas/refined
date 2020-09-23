package eu.timepit.refined.internal

import eu.timepit.refined.api.{RefType, Validate}
import eu.timepit.refined.macros.RefineMacro

/**
 * Helper class that allows the types `F`, `T`, and `P` to be inferred
 * from calls like `[[api.RefType.applyRefM]][F[T, P]](t)`.
 *
 * See [[http://tpolecat.github.io/2015/07/30/infer.html]] for a detailed
 * explanation of this trick.
 */
final class ApplyRefMPartiallyApplied[FTP] {

  def apply[F[_, _], T, P](t: T)(implicit
      ev: F[T, P] =:= FTP,
      rt: RefType[F],
      v: Validate[T, P]
  ): FTP = macro RefineMacro.implApplyRef[FTP, F, T, P]
}
