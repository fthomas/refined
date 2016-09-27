package eu.timepit.refined
package internal

import eu.timepit.refined.api.{RefType, Validate}
import eu.timepit.refined.macros.RefineMacro

final class RefineMFullyApplied[F[_, _], T, P] {

  def apply(t: T)(implicit rt: RefType[F], v: Validate[T, P]): F[T, P] =
    macro RefineMacro.impl[F, T, P]
}
