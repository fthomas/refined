package eu.timepit.refined
package internal

import eu.timepit.refined.api.RefinedType
import eu.timepit.refined.macros.RefineMacro

/**
 * Helper class that allows the type `T` to be inferred from calls like
 * `[[api.RefType.refineM]][P](t)`.
 *
 * See [[http://tpolecat.github.io/2015/07/30/infer.html]] for a detailed
 * explanation of this trick.
 */
final class RefineMPartiallyApplied[F[_, _], P] {

  def apply[T](t: T)(
    implicit
    rt: RefinedType.AuxT[F[T, P], T]
  ): F[T, P] = macro RefineMacro.refineImpl[F[T, P], T, P]
}
