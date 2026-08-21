package eu.timepit

import eu.timepit.refined.api.{RefType, Refined, Validate}
import eu.timepit.refined.internal._
import eu.timepit.refined.macros.Macros

package object refined {

  /**
   * Alias for `[[api.RefType.refine]][P]` with `[[api.Refined]]` as type
   * parameter for `[[api.RefType]]`.
   *
   * Note: `V` stands for '''v'''alue class.
   */
  def refineV[P]: RefinePartiallyApplied[Refined, P] = RefType.refinedRefType.refine[P]

  inline def refineMV[P]: RefineMVBuilder[P] = new RefineMVBuilder[P]

  final class RefineMVBuilder[P] {

    inline def apply[T](inline t: T)(implicit inline v: Validate[T, P]): Refined[T, P] =
      ${ Macros.refineMV[T, P]('t, 'v) }
  }

}
