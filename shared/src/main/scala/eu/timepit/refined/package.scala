package eu.timepit

import eu.timepit.refined.internal._
import shapeless.tag.@@

package object refined {

  /**
   * Alias for `shapeless.Witness` that provides concise syntax for
   * literal singleton types.
   *
   * Example: {{{
   * scala> val d: W.`3.14`.T = 3.14
   * d: Double(3.14) = 3.14
   *
   * scala> val s: W.`"abc"`.T = "abc"
   * s: String("abc") = abc
   * }}}
   *
   * See [[https://github.com/milessabin/shapeless/wiki/Feature-overview:-shapeless-2.0.0#singleton-typed-literals]]
   * for more information about shapeless' support for singleton types.
   */
  val W = shapeless.Witness

  /**
   * Alias for `[[RefType.refine]][P]` with `[[Refined]]` as type
   * parameter for `[[RefType]]`.
   *
   * Note: `V` stands for '''v'''alue class.
   */
  def refineV[P]: RefineAux[Refined, P] = RefType[Refined].refine[P]

  /**
   * Alias for `[[RefType.refine]][P]` with `shapeless.tag.@@` as type
   * parameter for `[[RefType]]`.
   *
   * Note: `T` stands for '''t'''ag.
   */
  def refineT[P]: RefineAux[@@, P] = RefType[@@].refine[P]

  /**
   * Alias for `[[RefType.refineM]][P]` with `[[Refined]]` as type
   * parameter for `[[RefType]]`.
   *
   * Note: `M` stands for '''m'''acro and `V` stands for '''v'''alue class.
   */
  def refineMV[P]: RefineMAux[Refined, P] = RefType[Refined].refineM[P]

  /**
   * Alias for `[[RefType.refineM]][P]` with `shapeless.tag.@@` as type
   * parameter for `[[RefType]]`.
   *
   * Note: `M` stands for '''m'''acro and `T` stands for '''t'''ag.
   */
  def refineMT[P]: RefineMAux[@@, P] = RefType[@@].refineM[P]
}
