package eu.timepit.refined.api

import eu.timepit.refined.macros.RefineMacro

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
 * scala> PosInt(1)
 * res0: PosInt = 1
 *
 * scala> PosInt.from(2)
 * res1: Either[String, PosInt] = Right(2)
 * }}}
 */
class RefinedTypeOps[FTP, T](implicit rt: RefinedType.AuxT[FTP, T]) extends Serializable {

  def apply[F[_, _], P](t: T)(
      implicit ev: F[T, P] =:= FTP,
      rt: RefType[F],
      v: Validate[T, P]
  ): FTP =
    macro RefineMacro.implApplyRef[FTP, F, T, P]

  def from(t: T): Either[String, FTP] =
    rt.refine(t)

  def unapply(t: T): Option[FTP] =
    from(t).right.toOption

  def unsafeFrom(t: T): FTP =
    rt.unsafeRefine(t)
}
