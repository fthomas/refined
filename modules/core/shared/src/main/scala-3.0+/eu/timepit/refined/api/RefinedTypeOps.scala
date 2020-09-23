package eu.timepit.refined.api

/**
 * Provides functions to create values of the refined type `FTP` from
 * values of the base type `T`. It is intended to simplify the definition
 * of a refined type's companion object.
 *
 * Example: {{{
 * scala> import eu.timepit.refined.api.{ Refined, RefinedTypeOps }
 *      | import eu.timepit.refined.numeric.Positive
 *
 * scala> type PosInt = Int Refined Positive
 *
 * scala> object PosInt extends RefinedTypeOps[PosInt, Int]
 *
 * scala> PosInt.from(1)
 * res0: Either[String, PosInt] = Right(1)
 *
 * scala> PosInt.unsafeFrom(2)
 * res1: PosInt = 2
 * }}}
 */
class RefinedTypeOps[FTP, T](implicit rt: RefinedType.AuxT[FTP, T]) extends Serializable {

  def from(t: T): Either[String, FTP] =
    rt.refine(t)

  def unapply(t: T): Option[FTP] =
    from(t).toOption

  def unsafeFrom(t: T): FTP =
    rt.unsafeRefine(t)
}

object RefinedTypeOps {
  class Numeric[FTP: Min: Max, T](implicit rt: RefinedType.AuxT[FTP, T])
      extends RefinedTypeOps[FTP, T] {

    /** The smallest valid value of this type */
    val MinValue: FTP = Min[FTP].min

    /** The largest valid value of this type */
    val MaxValue: FTP = Max[FTP].max
  }
}
