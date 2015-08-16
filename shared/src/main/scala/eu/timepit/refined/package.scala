package eu.timepit

import eu.timepit.refined.internal._
import shapeless.tag.@@

package object refined {

  /** An abbreviation for `shapeless.Witness`. */
  val W = shapeless.Witness

  /**
   * Returns `t` wrapped in `[[Refined]][T, P]` on the right if it satisfies
   * the predicate `P`, or an error message on the left otherwise.
   *
   * Example: {{{
   * scala> import eu.timepit.refined._
   *      | import eu.timepit.refined.numeric._
   *
   * scala> refineV[Positive](10)
   * res1: Either[String, Refined[Int, Positive]] = Right(Refined(10))
   * }}}
   *
   * Note: `V` stands for '''v'''alue class.
   *
   * Note: The return type is `[[internal.RefineAux]][P, Refined]`, which has
   * an `apply` method on it, allowing `refineV` to be called like in the
   * given example.
   */
  def refineV[P]: RefineAux[Refined, P] = RefType[Refined].refine[P]

  /**
   * Returns `t` with type `T @@ P` on the right if it satisfies the
   * predicate `P`, or an error message on the left otherwise.
   *
   * Example: {{{
   * scala> import eu.timepit.refined._
   *      | import eu.timepit.refined.numeric._
   *      | import shapeless.tag.@@
   *
   * scala> refineT[Positive](10)
   * res1: Either[String, Int @@ Positive] = Right(10)
   * }}}
   *
   * Note: `T` stands for '''t'''ag.
   *
   * Note: The return type is `[[internal.RefineAux]][P, @@]`, which has an
   * `apply` method on it, allowing `refineT` to be called like in the given
   * example.
   */
  def refineT[P]: RefineAux[@@, P] = RefType[@@].refine[P]

  /**
   * Macro that returns `t` wrapped in `[[Refined]][T, P]` if it satisfies
   * the predicate `P`, or fails to compile.
   *
   * Example: {{{
   * scala> import eu.timepit.refined._
   *      | import eu.timepit.refined.numeric._
   *
   * scala> refineMV[Positive](10)
   * res1: Refined[Int, Positive] = Refined(10)
   * }}}
   *
   * Note: `M` stands for '''m'''acro and `V` stands for '''v'''alue class.
   *
   * Note: The return type is `[[internal.RefineMAux]][P, Refined]`, which
   * has an `apply` method on it, allowing `refineMV` to be called like in
   * the given example.
   */
  def refineMV[P]: RefineMAux[Refined, P] = RefType[Refined].refineM[P]

  /**
   * Macro that returns `t` with type `T @@ P` if it satisfies the predicate
   * `P`, or fails to compile.
   *
   * Example: {{{
   * scala> import eu.timepit.refined._
   *      | import eu.timepit.refined.numeric._
   *      | import shapeless.tag.@@
   *
   * scala> refineMT[Positive](10)
   * res1: Int @@ Positive = 10
   * }}}
   *
   * Note: `M` stands for '''m'''acro and `T` stands for '''t'''ag.
   *
   * Note: The return type is `[[internal.RefineMAux]][P, @@]`, which has an
   * `apply` method on it, allowing `refineMT` to be called like in the given
   * example.
   */
  def refineMT[P]: RefineMAux[@@, P] = RefType[@@].refineM[P]

  @deprecated("refine is deprecated in favor of refineT", "0.2.0")
  def refine[P]: RefineAux[@@, P] = refineT[P]

  @deprecated("refineLit is deprecated in favor of refineMT", "0.2.0")
  def refineLit[P]: RefineMAux[@@, P] = refineMT[P]

  @deprecated("refineM is deprecated in favor of refineMV", "0.2.0")
  def refineM[P]: RefineMAux[Refined, P] = refineMV[P]
}
