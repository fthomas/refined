package eu.timepit.refined

import _root_.cats.{Contravariant, MonadError, Semigroup, Show}
import _root_.cats.implicits._
import _root_.cats.kernel.{CommutativeMonoid, CommutativeSemigroup, Eq, Monoid, Order}
import eu.timepit.refined.api.{RefType, Refined, Validate}
import eu.timepit.refined.numeric.{Negative, NonNegative, NonPositive, Positive}
import eu.timepit.refined.types.numeric._

package object cats {

  /**
   * `Eq` instance for refined types that delegates to the `Eq`
   * instance of the base type.
   */
  implicit def refTypeEq[F[_, _], T: Eq, P](implicit rt: RefType[F]): Eq[F[T, P]] =
    cats.derivation.refTypeViaContravariant[F, Eq, T, P]

  /**
   * `Order` instance for refined types that delegates to the `Order`
   * instance of the base type.
   */
  implicit def refTypeOrder[F[_, _], T: Order, P](implicit rt: RefType[F]): Order[F[T, P]] =
    cats.derivation.refTypeViaContravariant[F, Order, T, P]

  /**
   * `Show` instance for refined types that delegates to the `Show`
   * instance of the base type.
   */
  implicit def refTypeShow[F[_, _], T: Show, P](implicit rt: RefType[F]): Show[F[T, P]] =
    cats.derivation.refTypeViaContravariant[F, Show, T, P]

  // CommutativeSemigroup instances
  implicit val posByteCommutativeSemigroup: CommutativeSemigroup[PosByte] =
    getPosIntegralCommutativeSemigroup[Byte]
  implicit val posShortCommutativeSemigroup: CommutativeSemigroup[PosShort] =
    getPosIntegralCommutativeSemigroup[Short]
  implicit val posIntCommutativeSemigroup: CommutativeSemigroup[PosInt] =
    getPosIntegralCommutativeSemigroup[Int]
  implicit val posLongCommutativeSemigroup: CommutativeSemigroup[PosLong] =
    getPosIntegralCommutativeSemigroup[Long]
  implicit val posFloatCommutativeSemigroup: CommutativeSemigroup[PosFloat] =
    getCommutativeSemigroup[Float, Positive]
  implicit val posDoubleCommutativeSemigroup: CommutativeSemigroup[PosDouble] =
    getCommutativeSemigroup[Double, Positive]

  implicit val negByteCommutativeSemigroup: CommutativeSemigroup[NegByte] =
    getNegIntegralCommutativeSemigroup[Byte]
  implicit val negShortCommutativeSemigroup: CommutativeSemigroup[NegShort] =
    getNegIntegralCommutativeSemigroup[Short]
  implicit val negIntCommutativeSemigroup: CommutativeSemigroup[NegInt] =
    getNegIntegralCommutativeSemigroup[Int]
  implicit val negLongCommutativeSemigroup: CommutativeSemigroup[NegLong] =
    getNegIntegralCommutativeSemigroup[Long]
  implicit val negFloatCommutativeSemigroup: CommutativeSemigroup[NegFloat] =
    getCommutativeSemigroup[Float, Negative]
  implicit val negDoubleCommutativeSemigroup: CommutativeSemigroup[NegDouble] =
    getCommutativeSemigroup[Double, Negative]

  // Monoid instances
  implicit val nonNegByteCommutativeMonoid: CommutativeMonoid[NonNegByte] =
    getNonNegIntegralCommutativeMonoid[Byte]
  implicit val nonNegShortCommutativeMonoid: CommutativeMonoid[NonNegShort] =
    getNonNegIntegralCommutativeMonoid[Short]
  implicit val nonNegIntCommutativeMonoid: CommutativeMonoid[NonNegInt] =
    getNonNegIntegralCommutativeMonoid[Int]
  implicit val nonNegLongCommutativeMonoid: CommutativeMonoid[NonNegLong] =
    getNonNegIntegralCommutativeMonoid[Long]
  implicit val nonNegFloatCommutativeMonoid: CommutativeMonoid[NonNegFloat] =
    getCommutativeMonoid[Float, NonNegative]
  implicit val nonNegDoubleCommutativeMonoid: CommutativeMonoid[NonNegDouble] =
    getCommutativeMonoid[Double, NonNegative]

  implicit val nonPosFloatCommutativeMonoid: CommutativeMonoid[NonPosFloat] =
    getCommutativeMonoid[Float, NonPositive]
  implicit val nonPosDoubleCommutativeMonoid: CommutativeMonoid[NonPosDouble] =
    getCommutativeMonoid[Double, NonPositive]

  // Semigroup instances retained for binary compatibility
  val posByteSemigroup: Semigroup[PosByte] = posByteCommutativeSemigroup
  val posShortSemigroup: Semigroup[PosShort] = posShortCommutativeSemigroup
  val posIntSemigroup: Semigroup[PosInt] = posIntCommutativeSemigroup
  val posLongSemigroup: Semigroup[PosLong] = posLongCommutativeSemigroup
  val posFloatSemigroup: Semigroup[PosFloat] = posFloatCommutativeSemigroup
  val posDoubleSemigroup: Semigroup[PosDouble] = posDoubleCommutativeSemigroup

  val negByteSemigroup: Semigroup[NegByte] = negByteCommutativeSemigroup
  val negShortSemigroup: Semigroup[NegShort] = negShortCommutativeSemigroup
  val negIntSemigroup: Semigroup[NegInt] = negIntCommutativeSemigroup
  val negLongSemigroup: Semigroup[NegLong] = negLongCommutativeSemigroup
  val negFloatSemigroup: Semigroup[NegFloat] = negFloatCommutativeSemigroup
  val negDoubleSemigroup: Semigroup[NegDouble] = negDoubleCommutativeSemigroup

  // Monoid instances retained for binary compatibility
  val nonNegByteMonoid: Monoid[NonNegByte] = nonNegByteCommutativeMonoid
  val nonNegShortMonoid: Monoid[NonNegShort] = nonNegShortCommutativeMonoid
  val nonNegIntMonoid: Monoid[NonNegInt] = nonNegIntCommutativeMonoid
  val nonNegLongMonoid: Monoid[NonNegLong] = nonNegLongCommutativeMonoid
  val nonNegFloatMonoid: Monoid[NonNegFloat] = nonNegFloatCommutativeMonoid
  val nonNegDoubleMonoid: Monoid[NonNegDouble] = nonNegDoubleCommutativeMonoid

  val nonPosFloatMonoid: Monoid[NonPosFloat] = nonPosFloatCommutativeMonoid
  val nonPosDoubleMonoid: Monoid[NonPosDouble] = nonPosDoubleCommutativeMonoid

  private def getPosIntegralCommutativeSemigroup[A: CommutativeSemigroup: NonNegShift](implicit
      integral: Integral[A],
      v: Validate[A, Positive]
  ): CommutativeSemigroup[A Refined Positive] =
    CommutativeSemigroup.instance { (x, y) =>
      val combined: A = x.value |+| y.value

      refineV[Positive](combined).getOrElse {
        val result: A =
          CommutativeSemigroup[A].combine(NonNegShift[A].shift(combined), integral.one)
        refineV[Positive].unsafeFrom(result)
      }
    }

  private def getNegIntegralCommutativeSemigroup[A: Integral: CommutativeSemigroup: NegShift](
      implicit v: Validate[A, Negative]
  ): CommutativeSemigroup[A Refined Negative] =
    CommutativeSemigroup.instance { (x, y) =>
      val combined: A = x.value |+| y.value

      refineV[Negative](combined).getOrElse {
        val result: A = NegShift[A].shift(combined)
        refineV[Negative].unsafeFrom(result)
      }
    }

  private def getCommutativeSemigroup[A: CommutativeSemigroup, P](implicit
      v: Validate[A, P]
  ): CommutativeSemigroup[A Refined P] =
    CommutativeSemigroup.instance((x, y) => refineV[P].unsafeFrom(x.value |+| y.value))

  private def getNonNegIntegralCommutativeMonoid[A: Integral: CommutativeMonoid: NonNegShift](
      implicit v: Validate[A, NonNegative]
  ): CommutativeMonoid[A Refined NonNegative] =
    new CommutativeMonoid[A Refined NonNegative] {
      override def empty: A Refined NonNegative = refineV[NonNegative].unsafeFrom(Monoid[A].empty)

      override def combine(
          x: A Refined NonNegative,
          y: A Refined NonNegative
      ): A Refined NonNegative = {
        val combined: A = x.value |+| y.value

        refineV[NonNegative](combined).getOrElse {
          val result: A = NonNegShift[A].shift(combined)
          refineV[NonNegative].unsafeFrom(result)
        }
      }
    }

  private def getCommutativeMonoid[A: CommutativeMonoid, P](implicit
      v: Validate[A, P]
  ): CommutativeMonoid[A Refined P] =
    new CommutativeMonoid[A Refined P] {
      override def empty: A Refined P = refineV[P].unsafeFrom(Monoid[A].empty)

      override def combine(x: A Refined P, y: A Refined P): A Refined P =
        refineV[P].unsafeFrom(x.value |+| y.value)
    }

  @deprecated("Generic derivation instances have been moved into the `derivation` object", "0.9.4")
  def refTypeViaContravariant[F[_, _], G[_], T, P](implicit
      c: Contravariant[G],
      rt: RefType[F],
      gt: G[T]
  ): G[F[T, P]] =
    cats.derivation.refTypeViaContravariant[F, G, T, P]

  @deprecated("Generic derivation instances have been moved into the `derivation` object", "0.9.4")
  def refTypeViaMonadError[F[_, _], G[_], T, P](implicit
      m: MonadError[G, String],
      rt: RefType[F],
      v: Validate[T, P],
      gt: G[T]
  ): G[F[T, P]] =
    cats.derivation.refTypeViaMonadError[F, G, T, P]
}
