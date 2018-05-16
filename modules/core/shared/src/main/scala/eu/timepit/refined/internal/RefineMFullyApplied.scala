package eu.timepit.refined.internal

import eu.timepit.refined.api.{RefType, Validate}
import eu.timepit.refined.macros.RefineMacro

@deprecated(
  "RefineMFullyApplied has been replaced in favor or RefinedTypeOps. " +
    "Replace 'new RefineMFullyApplied[F, T, P]' with 'new RefinedTypeOps[F[T, P], T]'.",
  "0.9.1"
)
final class RefineMFullyApplied[F[_, _], T, P] {

  def apply(t: T)(implicit rt: RefType[F], v: Validate[T, P]): F[T, P] =
    macro RefineMacro.impl[F, T, P]
}
