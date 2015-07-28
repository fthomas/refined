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
   * @note The return type is `[[internal.Refine]][P, Refined]`, which has an
   * `apply` method on it, allowing `refineV` to be called like in the given
   * example.
   *
   * Example: {{{
   * scala> import eu.timepit.refined._
   *      | import eu.timepit.refined.numeric._
   *
   * scala> refineV[Positive](10)
   * res1: Either[String, Refined[Int, Positive]] = Right(Refined(10))
   * }}}
   */
  def refineV[P]: Refine[P, Refined] = new Refine[P, Refined]

  /**
   * Returns `t` with type `T @@ P` on the right if it satisfies the
   * predicate `P`, or an error message on the left otherwise.
   *
   * @note The return type is `[[internal.Refine]][P, @@]`, which has an
   * `apply` method on it, allowing `refineT` to be called like in the given
   * example.
   *
   * Example: {{{
   * scala> import eu.timepit.refined._
   *      | import eu.timepit.refined.numeric._
   *      | import shapeless.tag.@@
   *
   * scala> refineT[Positive](10)
   * res1: Either[String, Int @@ Positive] = Right(10)
   * }}}
   */
  def refineT[P]: Refine[P, @@] = new Refine[P, @@]

  /**
   * Macro that returns `t` wrapped in `[[Refined]][T, P]` if it satisfies
   * the predicate `P`, or fails to compile.
   *
   * @note The return type is `[[internal.RefineM]][P, Refined]`, which has
   * an `apply` method on it, allowing `refineMV` to be called like in the
   * given example.
   *
   * Example: {{{
   * scala> import eu.timepit.refined._
   *      | import eu.timepit.refined.numeric._
   *
   * scala> refineMV[Positive](10)
   * res1: Refined[Int, Positive] = Refined(10)
   * }}}
   */
  def refineMV[P]: RefineM[P, Refined] = new RefineM[P, Refined]

  /**
   * Macro that returns `t` with type `T @@ P` if it satisfies the predicate
   * `P`, or fails to compile.
   *
   * @note The return type is `[[internal.RefineM]][P, @@]`, which has an
   * `apply` method on it, allowing `refineMT` to be called like in the given
   * example.
   *
   * Example: {{{
   * scala> import eu.timepit.refined._
   *      | import eu.timepit.refined.numeric._
   *      | import shapeless.tag.@@
   *
   * scala> refineMT[Positive](10)
   * res1: Int @@ Positive = 10
   * }}}
   */
  def refineMT[P]: RefineM[P, @@] = new RefineM[P, @@]

  @deprecated("refine is deprecated in favor of refineT", "0.2.0")
  def refine[P]: Refine[P, @@] = new Refine[P, @@]

  @deprecated("refineLit is deprecated in favor of refineMT", "0.2.0")
  def refineLit[P]: RefineM[P, @@] = new RefineM[P, @@]

  @deprecated("refineM is deprecated in favor of refineMV", "0.2.0")
  def refineM[P]: RefineM[P, Refined] = new RefineM[P, Refined]
}
