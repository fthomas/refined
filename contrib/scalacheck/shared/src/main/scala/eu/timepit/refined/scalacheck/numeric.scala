package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.{RefType, Validate}
import eu.timepit.refined.numeric._
import eu.timepit.refined.scalacheck.util.{Adjacent, Bounded}
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Gen.Choose
import shapeless.{Nat, Witness}
import shapeless.ops.nat.ToInt

object numeric {

  /**
    * A generator that generates a random value in the given (inclusive)
    * range that satisfies the predicate `P`. If the range is invalid,
    * the generator will not generate any value.
    *
    * This is like ScalaCheck's `Gen.chooseNum` but for refined types.
    */
  def chooseRefinedNum[F[_, _], T : Numeric : Choose, P](
      min: F[T, P], max: F[T, P])(
      implicit rt: RefType[F],
      v: Validate[T, P]
  ): Gen[F[T, P]] =
    Gen
      .chooseNum(rt.unwrap(min), rt.unwrap(max))
      .filter(v.isValid)
      .map(rt.unsafeWrap)

  ///

  implicit def lessArbitraryWit[
      F[_, _]: RefType, T : Numeric : Choose : Adjacent, N <: T](
      implicit bounded: Bounded[T],
      wn: Witness.Aux[N]
  ): Arbitrary[F[T, Less[N]]] =
    rangeClosedOpenArbitrary(bounded.minValue, wn.value)

  implicit def lessArbitraryNat[
      F[_, _]: RefType, T : Choose : Adjacent, N <: Nat](
      implicit bounded: Bounded[T],
      nt: Numeric[T],
      tn: ToInt[N]
  ): Arbitrary[F[T, Less[N]]] =
    rangeClosedOpenArbitrary(bounded.minValue, nt.fromInt(tn()))

  implicit def lessEqualArbitraryWit[
      F[_, _]: RefType, T : Numeric : Choose, N <: T](
      implicit bounded: Bounded[T],
      wn: Witness.Aux[N]
  ): Arbitrary[F[T, LessEqual[N]]] =
    rangeClosedArbitrary(bounded.minValue, wn.value)

  implicit def lessEqualArbitraryNat[F[_, _]: RefType, T : Choose, N <: Nat](
      implicit bounded: Bounded[T],
      nt: Numeric[T],
      tn: ToInt[N]
  ): Arbitrary[F[T, LessEqual[N]]] =
    rangeClosedArbitrary(bounded.minValue, nt.fromInt(tn()))

  implicit def greaterArbitraryWit[
      F[_, _]: RefType, T : Numeric : Choose : Adjacent, N <: T](
      implicit bounded: Bounded[T],
      wn: Witness.Aux[N]
  ): Arbitrary[F[T, Greater[N]]] =
    rangeOpenClosedArbitrary(wn.value, bounded.maxValue)

  implicit def greaterArbitraryNat[
      F[_, _]: RefType, T : Choose : Adjacent, N <: Nat](
      implicit bounded: Bounded[T],
      nt: Numeric[T],
      tn: ToInt[N]
  ): Arbitrary[F[T, Greater[N]]] =
    rangeOpenClosedArbitrary(nt.fromInt(tn()), bounded.maxValue)

  implicit def greaterEqualArbitraryWit[
      F[_, _]: RefType, T : Numeric : Choose, N <: T](
      implicit bounded: Bounded[T],
      wn: Witness.Aux[N]
  ): Arbitrary[F[T, GreaterEqual[N]]] =
    rangeClosedArbitrary(wn.value, bounded.maxValue)

  implicit def greaterEqualArbitraryNat[
      F[_, _]: RefType, T : Choose, N <: Nat](
      implicit bounded: Bounded[T],
      nt: Numeric[T],
      tn: ToInt[N]
  ): Arbitrary[F[T, GreaterEqual[N]]] =
    rangeClosedArbitrary(nt.fromInt(tn()), bounded.maxValue)

  ///

  implicit def intervalOpenArbitrary[
      F[_, _]: RefType, T : Numeric : Choose : Adjacent, L <: T, H <: T](
      implicit wl: Witness.Aux[L],
      wh: Witness.Aux[H]
  ): Arbitrary[F[T, Interval.Open[L, H]]] =
    rangeOpenArbitrary(wl.value, wh.value)

  implicit def intervalOpenClosedArbitrary[
      F[_, _]: RefType, T : Numeric : Choose : Adjacent, L <: T, H <: T](
      implicit wl: Witness.Aux[L],
      wh: Witness.Aux[H]
  ): Arbitrary[F[T, Interval.OpenClosed[L, H]]] =
    rangeOpenClosedArbitrary(wl.value, wh.value)

  implicit def intervalClosedOpenArbitrary[
      F[_, _]: RefType, T : Numeric : Choose : Adjacent, L <: T, H <: T](
      implicit wl: Witness.Aux[L],
      wh: Witness.Aux[H]
  ): Arbitrary[F[T, Interval.ClosedOpen[L, H]]] =
    rangeClosedOpenArbitrary(wl.value, wh.value)

  implicit def intervalClosedArbitrary[
      F[_, _]: RefType, T : Numeric : Choose, L <: T, H <: T](
      implicit wl: Witness.Aux[L],
      wh: Witness.Aux[H]
  ): Arbitrary[F[T, Interval.Closed[L, H]]] =
    rangeClosedArbitrary(wl.value, wh.value)

  /// The following functions are private because it is not guaranteed
  /// that they produce valid values according to the predicate `P`.

  private def rangeOpenArbitrary[F[_, _]: RefType, T : Numeric : Choose, P](
      min: T, max: T)(
      implicit at: Adjacent[T]
  ): Arbitrary[F[T, P]] =
    arbitraryRefType(
        Gen.chooseNum(at.nextUpOrSelf(min), at.nextDownOrSelf(max)))

  private def rangeOpenClosedArbitrary[
      F[_, _]: RefType, T : Numeric : Choose, P](min: T, max: T)(
      implicit at: Adjacent[T]
  ): Arbitrary[F[T, P]] =
    arbitraryRefType(Gen.chooseNum(at.nextUpOrSelf(min), max))

  private def rangeClosedOpenArbitrary[
      F[_, _]: RefType, T : Numeric : Choose, P](min: T, max: T)(
      implicit at: Adjacent[T]
  ): Arbitrary[F[T, P]] =
    arbitraryRefType(Gen.chooseNum(min, at.nextDownOrSelf(max)))

  private def rangeClosedArbitrary[F[_, _]: RefType, T : Numeric : Choose, P](
      min: T, max: T): Arbitrary[F[T, P]] =
    arbitraryRefType(Gen.chooseNum(min, max))
}
