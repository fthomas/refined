package eu.timepit.refined
package internal

import eu.timepit.refined.api.{ RefType, Validate }
import eu.timepit.refined.macros.RefineMacro

final class RefineMFullyApplied[F[_, _], T, P] {

  def apply(t: T)(implicit v: Validate[T, P], rt: RefType[F]): F[T, P] = macro RefineMacro.macroImpl[F, T, P]
}
