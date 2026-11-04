package eu.timepit.refined.internal

import eu.timepit.refined.api.{Refined, Validate}
import eu.timepit.refined.macros.Macros

/**
 * Helper class that allows the types `T` and `P` to be inferred from calls
 * like `[[api.RefType.applyRefM]][F[T, P]](t)`.
 *
 * See [[http://tpolecat.github.io/2015/07/30/infer.html]] for a detailed
 * explanation of this trick.
 */
final class ApplyRefMPartiallyApplied[FTP] {

  inline def apply[T, P](inline t: T)(implicit
      inline ev: Refined[T, P] =:= FTP,
      inline v: Validate[T, P]
  ): FTP =
    ${ Macros.applyRef[FTP, T, P]('t, 'v) }
}
