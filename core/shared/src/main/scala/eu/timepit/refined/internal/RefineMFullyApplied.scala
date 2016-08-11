package eu.timepit.refined
package internal

import eu.timepit.refined.api.RefinedType
import eu.timepit.refined.macros.RefineMacro

final class RefineMFullyApplied[F[_, _], T, P] {

  def apply(t: T)(
    implicit
    rt: RefinedType.AuxT[F[T, P], T]
  ): F[T, P] = macro RefineMacro.refineImpl[F[T, P], T, P]
}
