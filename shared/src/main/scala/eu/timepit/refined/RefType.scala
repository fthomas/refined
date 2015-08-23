package eu.timepit.refined

import eu.timepit.refined.internal.{ RefineAux, RefineMAux }
import shapeless.tag.@@

import scala.reflect.macros.blackbox

/**
 * Type class that allows `F` to be used as result type of a refinement.
 * The first type parameter of `F` is the type that is being refined by
 * its second type parameter which is the type-level predicate that
 * denotes the refinement. Consequently, `F[T, P]` is a phantom type
 * that only contains a value of type `T`.
 *
 * The library provides instances of `RefType` for
 *  - the `[[Refined]]` value class
 *  - and `shapeless.tag.@@` which is a subtype of its first parameter
 *    (i.e. `(T @@ P) <: T`)
 */
trait RefType[F[_, _]] extends Serializable {

  def unsafeWrap[T, P](t: T): F[T, P]

  def unwrap[T, P](tp: F[T, P]): T

  def unsafeWrapM[T: c.WeakTypeTag, P: c.WeakTypeTag](c: blackbox.Context)(t: c.Expr[T]): c.Expr[F[T, P]]

  def unsafeRewrapM[T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](c: blackbox.Context)(ta: c.Expr[F[T, A]]): c.Expr[F[T, B]]

  /**
   * Returns a value of type `T` wrapped in `F[T, P]` on the right if
   * it satisfies the predicate `P`, or an error message on the left
   * otherwise.
   *
   * Example: {{{
   * scala> import eu.timepit.refined._
   *      | import eu.timepit.refined.numeric._
   *
   * scala> RefType[Refined].refine[Positive](10)
   * res1: Either[String, Refined[Int, Positive]] = Right(Refined(10))
   * }}}
   *
   * Note: The return type is `[[internal.RefineAux]][F, P]`, which has
   * an `apply` method on it, allowing `refine` to be called like in the
   * given example.
   */
  def refine[P]: RefineAux[F, P] =
    new RefineAux(this)

  /**
   * Macro that returns a value of type `T` wrapped in `F[T, P]` if it
   * satisfies the predicate `P`, or fails to compile otherwise.
   *
   * Example: {{{
   * scala> import eu.timepit.refined._
   *      | import eu.timepit.refined.numeric._
   *
   * scala> RefType[Refined].refineM[Positive](10)
   * res1: Refined[Int, Positive] = Refined(10)
   * }}}
   *
   * Note: `M` stands for '''m'''acro.
   *
   * Note: The return type is `[[internal.RefineMAux]][F, P]`, which has
   * an `apply` method on it, allowing `refineM` to be called like in the
   * given example.
   */
  def refineM[P]: RefineMAux[F, P] =
    new RefineMAux

  def mapRefine[T, P, U](tp: F[T, P])(f: T => U)(implicit p: Predicate[P, U]): Either[String, F[U, P]] =
    refine(f(unwrap(tp)))
}

object RefType {

  def apply[F[_, _]](implicit rt: RefType[F]): RefType[F] = rt

  implicit val refinedRefType: RefType[Refined] =
    new RefType[Refined] {
      override def unsafeWrap[T, P](t: T): Refined[T, P] =
        Refined.unsafeApply(t)

      override def unwrap[T, P](tp: Refined[T, P]): T =
        tp.get

      override def unsafeWrapM[T: c.WeakTypeTag, P: c.WeakTypeTag](c: blackbox.Context)(t: c.Expr[T]): c.Expr[Refined[T, P]] =
        c.universe.reify(Refined.unsafeApply(t.splice))

      override def unsafeRewrapM[T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](c: blackbox.Context)(ta: c.Expr[Refined[T, A]]): c.Expr[Refined[T, B]] =
        c.universe.reify(ta.splice.asInstanceOf[Refined[T, B]])
    }

  implicit val tagRefType: RefType[@@] =
    new RefType[@@] {
      override def unsafeWrap[T, P](t: T): T @@ P =
        t.asInstanceOf[T @@ P]

      override def unwrap[T, P](tp: T @@ P): T =
        tp

      override def unsafeWrapM[T: c.WeakTypeTag, P: c.WeakTypeTag](c: blackbox.Context)(t: c.Expr[T]): c.Expr[T @@ P] =
        c.universe.reify(t.splice.asInstanceOf[T @@ P])

      override def unsafeRewrapM[T: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag](c: blackbox.Context)(ta: c.Expr[T @@ A]): c.Expr[T @@ B] =
        c.universe.reify(ta.splice.asInstanceOf[T @@ B])
    }

  final class RefTypeOps[F[_, _], T, P](tp: F[T, P])(implicit F: RefType[F]) {
    def unwrap: T =
      F.unwrap(tp)

    def mapRefine[U](f: T => U)(implicit p: Predicate[P, U]): Either[String, F[U, P]] =
      F.mapRefine(tp)(f)
  }

  object ops {
    implicit def toRefTypeOps[F[_, _]: RefType, T, P](tp: F[T, P]): RefTypeOps[F, T, P] =
      new RefTypeOps(tp)
  }
}
