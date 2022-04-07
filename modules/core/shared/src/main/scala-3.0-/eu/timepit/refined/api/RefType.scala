package eu.timepit.refined.api

import eu.timepit.refined.internal._
import shapeless.tag.@@

/**
 * Type class that allows `F` to be used as carrier type of a refinement.
 * The first type parameter of `F` is the base type that is being refined
 * by its second type parameter which is the type-level predicate that
 * denotes the refinement. Consequently, `F[T, P]` is a phantom type
 * that only contains a value of type `T`.
 *
 * The library provides instances of `[[RefType]]` for
 *  - the `[[Refined]]` value class
 *  - and `shapeless.tag.@@` which is a subtype of its first parameter
 *    (i.e. `(T @@ P) <: T`)
 */
trait RefType[F[_, _]] extends Serializable {

  def unsafeWrap[T, P](t: T): F[T, P]

  def unwrap[T, P](tp: F[T, P]): T

  def unsafeRewrap[T, A, B](ta: F[T, A]): F[T, B]

  /**
   * Returns a value of type `T` refined as `F[T, P]` on the right if
   * it satisfies the predicate `P`, or an error message on the left
   * otherwise.
   *
   * Example: {{{
   * scala> import eu.timepit.refined.api.{ Refined, RefType }
   *      | import eu.timepit.refined.numeric.Positive
   *
   * scala> RefType[Refined].refine[Positive](10)
   * res0: Either[String, Refined[Int, Positive]] = Right(10)
   * }}}
   *
   * Note: The return type is `[[internal.RefinePartiallyApplied]][F, P]`,
   * which has an `apply` method on it, allowing the type `T` to be
   * inferred from its argument.
   */
  def refine[P]: RefinePartiallyApplied[F, P] =
    new RefinePartiallyApplied(this)

  /**
   * Macro that returns a value of type `T` refined as `F[T, P]` if
   * it satisfies the predicate `P`, or fails to compile otherwise.
   *
   * Example: {{{
   * scala> import eu.timepit.refined.api.{ Refined, RefType }
   *      | import eu.timepit.refined.numeric.Positive
   *
   * scala> RefType[Refined].refineM[Positive](10)
   * res0: Refined[Int, Positive] = 10
   * }}}
   *
   * Note: `M` stands for '''m'''acro.
   *
   * Note: The return type is `[[internal.RefineMPartiallyApplied]][F, P]`,
   * which has an `apply` method on it, allowing the type `T` to be
   * inferred from its argument.
   */
  def refineM[P]: RefineMPartiallyApplied[F, P] =
    new RefineMPartiallyApplied

  def mapRefine[T, P, U](
      tp: F[T, P]
  )(f: T => U)(implicit v: Validate[U, P]): Either[String, F[U, P]] =
    refine(f(unwrap(tp)))

  def coflatMapRefine[T, P, U](
      tp: F[T, P]
  )(f: F[T, P] => U)(implicit v: Validate[U, P]): Either[String, F[U, P]] =
    refine(f(tp))
}

object RefType {

  /** Returns a `RefType` for the given type `F` from the implicit scope. */
  def apply[F[_, _]](implicit rt: RefType[F]): RefType[F] = rt

  /**
   * Returns a value of type `T` refined as `FTP` on the right if it
   * satisfies the predicate in `FTP`, or an error message on the left
   * otherwise.
   *
   * Example: {{{
   * scala> import eu.timepit.refined.api.{ Refined, RefType }
   *      | import eu.timepit.refined.numeric.Positive
   *
   * scala> type PosInt = Int Refined Positive
   * scala> RefType.applyRef[PosInt](10)
   * res0: Either[String, PosInt] = Right(10)
   * }}}
   *
   * Note: The return type is `[[internal.ApplyRefPartiallyApplied]][FTP]`,
   * which has an `apply` method on it, allowing `applyRef` to be called
   * like in the given example.
   */
  def applyRef[FTP]: ApplyRefPartiallyApplied[FTP] =
    new ApplyRefPartiallyApplied

  /**
   * Macro that returns a value of type `T` refined as `FTP` if  it
   * satisfies the predicate in `FTP`, or fails to compile otherwise.
   *
   * Example: {{{
   * scala> import eu.timepit.refined.api.{ Refined, RefType }
   *      | import eu.timepit.refined.numeric.Positive
   *
   * scala> type PosInt = Int Refined Positive
   * scala> RefType.applyRefM[PosInt](10)
   * res0: PosInt = 10
   * }}}
   *
   * Note: `M` stands for '''m'''acro.
   *
   * Note: The return type is `[[internal.ApplyRefMPartiallyApplied]][FTP]`,
   * which has an `apply` method on it, allowing `applyRefM` to be called
   * like in the given example.
   */
  def applyRefM[FTP]: ApplyRefMPartiallyApplied[FTP] =
    new ApplyRefMPartiallyApplied

  implicit val refinedRefType: RefType[Refined] =
    new RefType[Refined] {
      override def unsafeWrap[T, P](t: T): Refined[T, P] =
        Refined.unsafeApply(t)

      override def unwrap[T, P](tp: Refined[T, P]): T =
        tp.value

      override def unsafeRewrap[T, A, B](ta: Refined[T, A]): Refined[T, B] =
        Refined.unsafeApply(ta.value)
    }

  implicit val tagRefType: RefType[@@] =
    new RefType[@@] {
      override def unsafeWrap[T, P](t: T): T @@ P =
        t.asInstanceOf[T @@ P]

      override def unwrap[T, P](tp: T @@ P): T =
        tp

      override def unsafeRewrap[T, A, B](ta: T @@ A): T @@ B =
        ta.asInstanceOf[T @@ B]
    }

  final class RefTypeOps[F[_, _], T, P](tp: F[T, P])(implicit F: RefType[F]) {
    def unwrap: T =
      F.unwrap(tp)

    def mapRefine[U](f: T => U)(implicit v: Validate[U, P]): Either[String, F[U, P]] =
      F.mapRefine(tp)(f)

    def coflatMapRefine[U](f: F[T, P] => U)(implicit v: Validate[U, P]): Either[String, F[U, P]] =
      F.coflatMapRefine(tp)(f)
  }

  object ops {
    implicit def toRefTypeOps[F[_, _]: RefType, T, P](tp: F[T, P]): RefTypeOps[F, T, P] =
      new RefTypeOps(tp)
  }
}
