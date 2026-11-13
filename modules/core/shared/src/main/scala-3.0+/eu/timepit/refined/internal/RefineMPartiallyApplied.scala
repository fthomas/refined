package eu.timepit.refined.internal

import eu.timepit.refined.api.{RefType, Validate}
import eu.timepit.refined.macros.Macros

/**
 * Helper class that allows the type `T` to be inferred from calls like
 * `[[api.RefType.refineM]][P](t)`.
 *
 * See [[http://tpolecat.github.io/2015/07/30/infer.html]] for a detailed
 * explanation of this trick.
 */
final class RefineMPartiallyApplied[F[_, _], P] {

  // The macro only validates `t` against `P` at compile time (returning `t`); wrapping into `F[T, P]`
  // is the zero-cost runtime `unsafeWrap`. This keeps the macro carrier-agnostic, so no higher-kinded
  // macro over `F` is needed. `apply` is inline (not itself a macro) so it may combine the macro call
  // with `unsafeWrap` — a macro's splice must be the entire right-hand side, which `validated` is.
  inline def apply[T](inline t: T)(implicit rt: RefType[F], inline v: Validate[T, P]): F[T, P] =
    rt.unsafeWrap[T, P](validated[T](t))

  private inline def validated[T](inline t: T)(implicit inline v: Validate[T, P]): T =
    ${ Macros.refineM[T, P]('t, 'v) }
}
