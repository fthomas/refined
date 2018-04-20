package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.{Max, Min, RefType, Validate}
import eu.timepit.refined.internal.{Adjacent, AsValueOf}
import eu.timepit.refined.numeric._
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Gen.Choose

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

  implicit def lessArbitrary[F[_, _]: RefType, T: Numeric: Choose: Adjacent, N](
      implicit
      min: Min[T],
      vn: AsValueOf[N, T]
  ): Arbitrary[F[T, Less[N]]] =
    rangeClosedOpenArbitrary(min.min, vn.snd)

  implicit def lessEqualArbitrary[F[_, _]: RefType, T: Numeric: Choose, N](
      implicit
      min: Min[T],
      vn: AsValueOf[N, T]
  ): Arbitrary[F[T, LessEqual[N]]] =
    rangeClosedArbitrary(min.min, vn.snd)

  implicit def greaterArbitrary[F[_, _]: RefType, T: Numeric: Choose: Adjacent, N](
      implicit
      max: Max[T],
      vn: AsValueOf[N, T]
  ): Arbitrary[F[T, Greater[N]]] =
    rangeOpenClosedArbitrary(vn.snd, max.max)

  implicit def greaterEqualArbitrary[F[_, _]: RefType, T: Numeric: Choose, N](
      implicit
      max: Max[T],
      vn: AsValueOf[N, T]
  ): Arbitrary[F[T, GreaterEqual[N]]] =
    rangeClosedArbitrary(vn.snd, max.max)

  ///

  implicit def intervalOpenArbitrary[F[_, _]: RefType, T: Numeric: Choose: Adjacent, L, H](
      implicit
      vl: AsValueOf[L, T],
      vh: AsValueOf[H, T]
  ): Arbitrary[F[T, Interval.Open[L, H]]] =
    rangeOpenArbitrary(vl.snd, vh.snd)

  implicit def intervalOpenClosedArbitrary[F[_, _]: RefType, T: Numeric: Choose: Adjacent, L, H](
      implicit
      vl: AsValueOf[L, T],
      vh: AsValueOf[H, T]
  ): Arbitrary[F[T, Interval.OpenClosed[L, H]]] =
    rangeOpenClosedArbitrary(vl.snd, vh.snd)

  implicit def intervalClosedOpenArbitrary[F[_, _]: RefType, T: Numeric: Choose: Adjacent, L, H](
      implicit
      vl: AsValueOf[L, T],
      vh: AsValueOf[H, T]
  ): Arbitrary[F[T, Interval.ClosedOpen[L, H]]] =
    rangeClosedOpenArbitrary(vl.snd, vh.snd)

  implicit def intervalClosedArbitrary[F[_, _]: RefType, T: Numeric: Choose, L, H](
      implicit
      vl: AsValueOf[L, T],
      vh: AsValueOf[H, T]
  ): Arbitrary[F[T, Interval.Closed[L, H]]] =
    rangeClosedArbitrary(vl.snd, vh.snd)

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
