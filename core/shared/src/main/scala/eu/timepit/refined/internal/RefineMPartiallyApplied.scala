package eu.timepit.refined
package internal

import eu.timepit.refined.api.{ RefType, Validate }

/**
 * Helper class that allows the type `T` to be inferred from calls like
 * `[[api.RefType.refineM]][P](t)`.
 *
 * See [[http://tpolecat.github.io/2015/07/30/infer.html]] for a detailed
 * explanation of this trick.
 */
final class RefineMPartiallyApplied[F[_, _], P] {

  def apply[T](t: T)(implicit v: Validate[T, P], rt: RefType[F]): F[T, P] = macro RefineM.macroImpl[F, T, P]
}
