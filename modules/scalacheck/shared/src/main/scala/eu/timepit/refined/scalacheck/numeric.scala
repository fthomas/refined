package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.{Max, Min, RefType, Validate}
import eu.timepit.refined.internal.Adjacent
import eu.timepit.refined.numeric._
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Gen.Choose
import shapeless.{Nat, Witness}
import shapeless.ops.nat.ToInt

/**
 * Module that provides `Arbitrary` instances and generators for
 * numeric predicates.
 */
object numeric extends NumericInstances

trait NumericInstances {

  /**
   * A generator that generates a random value in the given (inclusive)
   * range that satisfies the predicate `P`. If the range is invalid,
   * the generator will not generate any value.
   *
   * This is like ScalaCheck's `Gen.chooseNum` but for refined types.
   */
  def chooseRefinedNum[F[_, _], T: Numeric: Choose, P](min: F[T, P], max: F[T, P])(
      implicit rt: RefType[F],
      v: Validate[T, P]
  ): Gen[F[T, P]] =
    Gen.chooseNum(rt.unwrap(min), rt.unwrap(max)).filter(v.isValid).map(rt.unsafeWrap)

  ///

  implicit def lessArbitraryWit[F[_, _]: RefType, T: Numeric: Choose: Adjacent, N <: T](
      implicit min: Min[T],
      wn: Witness.Aux[N]
  ): Arbitrary[F[T, Less[N]]] =
    rangeClosedOpenArbitrary(min.min, wn.value)

  implicit def lessArbitraryNat[F[_, _]: RefType, T: Choose: Adjacent, N <: Nat](
      implicit min: Min[T],
      nt: Numeric[T],
      tn: ToInt[N]
  ): Arbitrary[F[T, Less[N]]] =
    rangeClosedOpenArbitrary(min.min, nt.fromInt(tn()))

  implicit def lessEqualArbitraryWit[F[_, _]: RefType, T: Numeric: Choose, N <: T](
      implicit min: Min[T],
      wn: Witness.Aux[N]
  ): Arbitrary[F[T, LessEqual[N]]] =
    rangeClosedArbitrary(min.min, wn.value)

  implicit def lessEqualArbitraryNat[F[_, _]: RefType, T: Choose, N <: Nat](
      implicit min: Min[T],
      nt: Numeric[T],
      tn: ToInt[N]
  ): Arbitrary[F[T, LessEqual[N]]] =
    rangeClosedArbitrary(min.min, nt.fromInt(tn()))

  implicit def greaterArbitraryWit[F[_, _]: RefType, T: Numeric: Choose: Adjacent, N <: T](
      implicit max: Max[T],
      wn: Witness.Aux[N]
  ): Arbitrary[F[T, Greater[N]]] =
    rangeOpenClosedArbitrary(wn.value, max.max)

  implicit def greaterArbitraryNat[F[_, _]: RefType, T: Choose: Adjacent, N <: Nat](
      implicit max: Max[T],
      nt: Numeric[T],
      tn: ToInt[N]
  ): Arbitrary[F[T, Greater[N]]] =
    rangeOpenClosedArbitrary(nt.fromInt(tn()), max.max)

  implicit def greaterEqualArbitraryWit[F[_, _]: RefType, T: Numeric: Choose, N <: T](
      implicit max: Max[T],
      wn: Witness.Aux[N]
  ): Arbitrary[F[T, GreaterEqual[N]]] =
    rangeClosedArbitrary(wn.value, max.max)

  implicit def greaterEqualArbitraryNat[F[_, _]: RefType, T: Choose, N <: Nat](
      implicit max: Max[T],
      nt: Numeric[T],
      tn: ToInt[N]
  ): Arbitrary[F[T, GreaterEqual[N]]] =
    rangeClosedArbitrary(nt.fromInt(tn()), max.max)

  ///

  implicit def intervalOpenArbitrary[F[_, _]: RefType,
                                     T: Numeric: Choose: Adjacent,
                                     L <: T,
                                     H <: T](
      implicit wl: Witness.Aux[L],
      wh: Witness.Aux[H]
  ): Arbitrary[F[T, Interval.Open[L, H]]] =
    rangeOpenArbitrary(wl.value, wh.value)

  implicit def intervalOpenClosedArbitrary[F[_, _]: RefType,
                                           T: Numeric: Choose: Adjacent,
                                           L <: T,
                                           H <: T](
      implicit wl: Witness.Aux[L],
      wh: Witness.Aux[H]
  ): Arbitrary[F[T, Interval.OpenClosed[L, H]]] =
    rangeOpenClosedArbitrary(wl.value, wh.value)

  implicit def intervalClosedOpenArbitrary[F[_, _]: RefType,
                                           T: Numeric: Choose: Adjacent,
                                           L <: T,
                                           H <: T](
      implicit wl: Witness.Aux[L],
      wh: Witness.Aux[H]
  ): Arbitrary[F[T, Interval.ClosedOpen[L, H]]] =
    rangeClosedOpenArbitrary(wl.value, wh.value)

  implicit def intervalClosedArbitrary[F[_, _]: RefType, T: Numeric: Choose, L <: T, H <: T](
      implicit wl: Witness.Aux[L],
      wh: Witness.Aux[H]
  ): Arbitrary[F[T, Interval.Closed[L, H]]] =
    rangeClosedArbitrary(wl.value, wh.value)

  /// The following functions are private because it is not guaranteed
  /// that they produce valid values according to the predicate `P`.

  private def rangeOpenArbitrary[F[_, _]: RefType, T: Numeric: Choose, P](min: T, max: T)(
      implicit at: Adjacent[T]
  ): Arbitrary[F[T, P]] =
    arbitraryRefType(Gen.chooseNum(at.nextUp(min), at.nextDown(max)))

  private def rangeOpenClosedArbitrary[F[_, _]: RefType, T: Numeric: Choose, P](min: T, max: T)(
      implicit at: Adjacent[T]
  ): Arbitrary[F[T, P]] =
    arbitraryRefType(Gen.chooseNum(at.nextUp(min), max))

  private def rangeClosedOpenArbitrary[F[_, _]: RefType, T: Numeric: Choose, P](min: T, max: T)(
      implicit at: Adjacent[T]
  ): Arbitrary[F[T, P]] =
    arbitraryRefType(Gen.chooseNum(min, at.nextDown(max)))

  private def rangeClosedArbitrary[F[_, _]: RefType, T: Numeric: Choose, P](
      min: T,
      max: T): Arbitrary[F[T, P]] =
    arbitraryRefType(Gen.chooseNum(min, max))
}
