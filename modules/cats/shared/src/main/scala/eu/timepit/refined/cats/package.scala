package eu.timepit.refined

import _root_.cats.{Contravariant, MonadError, Show}
import _root_.cats.implicits._
import _root_.cats.kernel.{Eq, Monoid, Order, Semigroup}
import eu.timepit.refined.api.{Refined, RefType, Validate}
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

  // Semigroup instances
  implicit val posByteSemigroup: Semigroup[PosByte] = getPosIntegralSemigroup[Byte]
  implicit val posShortSemigroup: Semigroup[PosShort] = getPosIntegralSemigroup[Short]
  implicit val posIntSemigroup: Semigroup[PosInt] = getPosIntegralSemigroup[Int]
  implicit val posLongSemigroup: Semigroup[PosLong] = getPosIntegralSemigroup[Long]
  implicit val posFloatSemigroup: Semigroup[PosFloat] = getSemigroup[Float, Positive]
  implicit val posDoubleSemigroup: Semigroup[PosDouble] = getSemigroup[Double, Positive]

  implicit val negByteSemigroup: Semigroup[NegByte] = getNegIntegralSemigroup[Byte]
  implicit val negShortSemigroup: Semigroup[NegShort] = getNegIntegralSemigroup[Short]
  implicit val negIntSemigroup: Semigroup[NegInt] = getNegIntegralSemigroup[Int]
  implicit val negLongSemigroup: Semigroup[NegLong] = getNegIntegralSemigroup[Long]
  implicit val negFloatSemigroup: Semigroup[NegFloat] = getSemigroup[Float, Negative]
  implicit val negDoubleSemigroup: Semigroup[NegDouble] = getSemigroup[Double, Negative]

  // Monoid instances
  implicit val nonNegByteMonoid: Monoid[NonNegByte] = getNonNegIntegralMonoid[Byte]
  implicit val nonNegShortMonoid: Monoid[NonNegShort] = getNonNegIntegralMonoid[Short]
  implicit val nonNegIntMonoid: Monoid[NonNegInt] = getNonNegIntegralMonoid[Int]
  implicit val nonNegLongMonoid: Monoid[NonNegLong] = getNonNegIntegralMonoid[Long]
  implicit val nonNegFloatMonoid: Monoid[NonNegFloat] = getMonoid[Float, NonNegative]
  implicit val nonNegDoubleMonoid: Monoid[NonNegDouble] = getMonoid[Double, NonNegative]

  implicit val nonPosFloatMonoid: Monoid[NonPosFloat] = getMonoid[Float, NonPositive]
  implicit val nonPosDoubleMonoid: Monoid[NonPosDouble] = getMonoid[Double, NonPositive]

  private def getPosIntegralSemigroup[A: Semigroup: NonNegShift](implicit
      integral: Integral[A],
      v: Validate[A, Positive]
  ): Semigroup[A Refined Positive] =
    Semigroup.instance { (x, y) =>
      val combined: A = x.value |+| y.value

      refineV[Positive](combined).getOrElse {
        val result: A = Semigroup[A].combine(NonNegShift[A].shift(combined), integral.one)
        refineV[Positive].unsafeFrom(result)
      }
    }

  private def getNegIntegralSemigroup[A: Integral: Semigroup: NegShift](implicit
      v: Validate[A, Negative]
  ): Semigroup[A Refined Negative] =
    Semigroup.instance { (x, y) =>
      val combined: A = x.value |+| y.value

      refineV[Negative](combined).getOrElse {
        val result: A = NegShift[A].shift(combined)
        refineV[Negative].unsafeFrom(result)
      }
    }

  private def getSemigroup[A: Semigroup, P](implicit
      v: Validate[A, P]
  ): Semigroup[A Refined P] =
    Semigroup.instance((x, y) => refineV[P].unsafeFrom(x.value |+| y.value))

  private def getNonNegIntegralMonoid[A: Integral: Monoid: NonNegShift](implicit
      v: Validate[A, NonNegative]
  ): Monoid[A Refined NonNegative] =
    new Monoid[A Refined NonNegative] {
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

  private def getMonoid[A: Monoid, P](implicit
      v: Validate[A, P]
  ): Monoid[A Refined P] =
    new Monoid[A Refined P] {
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
