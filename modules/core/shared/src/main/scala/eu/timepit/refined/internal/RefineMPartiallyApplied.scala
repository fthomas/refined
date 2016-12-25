package eu.timepit.refined
package internal

import eu.timepit.refined.api.{RefType, Validate}
import eu.timepit.refined.macros.RefineMacro

/**
 * Helper class that allows the type `T` to be inferred from calls like
 * `[[api.RefType.refineM]][P](t)`.
 *
 * See [[http://tpolecat.github.io/2015/07/30/infer.html]] for a detailed
 * explanation of this trick.
 */
final class RefineMPartiallyApplied[F[_, _], P] {

  def apply[T](t: T)(implicit rt: RefType[F], v: Validate[T, P]): F[T, P] =
    macro RefineMacro.impl[F, T, P]
}
