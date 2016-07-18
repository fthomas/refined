package eu.timepit

import eu.timepit.refined.api.{ RefType, Refined }
import eu.timepit.refined.internal._
import eu.timepit.refined.numeric.NonNegative
import shapeless.tag.@@

package object refined {

  type Natural = Int Refined NonNegative

  /**
   * Alias for `shapeless.Witness` that provides concise syntax for
   * literal-based singleton types.
   *
   * Example: {{{
   * scala> val d: W.`3.14`.T = 3.14
   * d: Double(3.14) = 3.14
   *
   * scala> val s: W.`"abc"`.T = "abc"
   * s: String("abc") = abc
   * }}}
   *
   * See the [[https://github.com/milessabin/shapeless/wiki/Feature-overview:-shapeless-2.0.0#singleton-typed-literals shapeless wiki]]
   * for more information about its support for singleton types.
   *
   * Note that if a future version of Scala implements
   * [[http://docs.scala-lang.org/sips/pending/42.type.html SIP-23]],
   * `shapeless.Witness` won't be necessary anymore to express
   * literal-based singleton types. It will then be possible to use
   * literals directly in a position where a type is expected.
   */
  val W = shapeless.Witness

  /**
   * Alias for `[[api.RefType.refine]][P]` with `[[api.Refined]]` as type
   * parameter for `[[api.RefType]]`.
   *
   * Note: `V` stands for '''v'''alue class.
   */
  def refineV[P]: RefinePartiallyApplied[Refined, P] = RefType[Refined].refine[P]

  /**
   * Alias for `[[api.RefType.refine]][P]` with `shapeless.tag.@@` as type
   * parameter for `[[api.RefType]]`.
   *
   * Note: `T` stands for '''t'''ag.
   */
  def refineT[P]: RefinePartiallyApplied[@@, P] = RefType[@@].refine[P]

  /**
   * Alias for `[[api.RefType.refineM]][P]` with `[[api.Refined]]` as type
   * parameter for `[[api.RefType]]`.
   *
   * Note: `M` stands for '''m'''acro and `V` stands for '''v'''alue class.
   */
  def refineMV[P]: RefineMPartiallyApplied[Refined, P] = RefType[Refined].refineM[P]

  /**
   * Alias for `[[api.RefType.refineM]][P]` with `shapeless.tag.@@` as type
   * parameter for `[[api.RefType]]`.
   *
   * Note: `M` stands for '''m'''acro and `T` stands for '''t'''ag.
   */
  def refineMT[P]: RefineMPartiallyApplied[@@, P] = RefType[@@].refineM[P]
}
