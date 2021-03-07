package eu.timepit

import eu.timepit.refined.api.{Refined, RefType}
import eu.timepit.refined.internal._
import shapeless.tag.@@

package object refined {

  /**
   * Alias for `[[api.RefType.refine]][P]` with `[[api.Refined]]` as type
   * parameter for `[[api.RefType]]`.
   *
   * Note: `V` stands for '''v'''alue class.
   */
  def refineV[P]: RefinePartiallyApplied[Refined, P] = RefType.refinedRefType.refine[P]

  trait Predicate[T, P] {
    inline def isValid(inline t: T): Boolean
  }

  class Positive1
  object Positive1 {
    inline given Predicate[Int, Positive1] with
      inline def isValid(inline t: Int): Boolean = t > 0
  }

  class NonEmpty1
  object NonEmpty1 {
    inline given Predicate[String, NonEmpty1] with
      inline def isValid(inline s: String): Boolean = !(s == "")
    // If we use !s.isEmpty here, we get the following compile error:
    // |Cannot reduce `inline if` because its condition is not a constant value: "hello".isEmpty().unary_! :Boolean
  }

  inline def refineMV[T, P](inline t: T)(using inline p: Predicate[T, P]): Refined[T, P] = {
    inline if (p.isValid(t)) Refined.unsafeApply(t) else scala.compiletime.error("no")
  }

  /**
   * Alias for `[[api.RefType.refine]][P]` with `shapeless.tag.@@` as type
   * parameter for `[[api.RefType]]`.
   *
   * Note: `T` stands for '''t'''ag.
   */
  def refineT[P]: RefinePartiallyApplied[@@, P] = RefType.tagRefType.refine[P]
}
