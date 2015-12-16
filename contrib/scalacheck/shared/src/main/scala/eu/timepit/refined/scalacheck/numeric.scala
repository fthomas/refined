package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.numeric._
import org.scalacheck.Gen.Choose
import org.scalacheck.{ Arbitrary, Gen }
import shapeless.ops.nat.ToInt
import shapeless.{ Nat, Witness }

object numeric {

  trait Bounded[T] {
    def minValue: T
    def maxValue: T
  }

  object Bounded {
    def apply[T](min: T, max: T): Bounded[T] =
      new Bounded[T] {
        val minValue = min
        val maxValue = max
      }

    implicit val byte: Bounded[Byte] =
      Bounded(Byte.MinValue, Byte.MaxValue)

    implicit val char: Bounded[Char] =
      Bounded(Char.MinValue, Char.MaxValue)

    implicit val double: Bounded[Double] =
      Bounded(Double.MinValue, Double.MaxValue)

    implicit val float: Bounded[Float] =
      Bounded(Float.MinValue, Float.MaxValue)

    implicit val int: Bounded[Int] =
      Bounded(Int.MinValue, Int.MaxValue)

    implicit val long: Bounded[Long] =
      Bounded(Long.MinValue, Long.MaxValue)

    implicit val short: Bounded[Short] =
      Bounded(Short.MinValue, Short.MaxValue)
  }

  ///

  implicit def lessArbitraryWit[F[_, _]: RefType, T: Numeric: Choose, N <: T](
    implicit
    bounded: Bounded[T],
    wn: Witness.Aux[N]
  ): Arbitrary[F[T, Less[N]]] =
    rangeClosedOpenArbitrary(bounded.minValue, wn.value)

  implicit def lessArbitraryNat[F[_, _]: RefType, T: Choose, N <: Nat](
    implicit
    bounded: Bounded[T],
    nt: Numeric[T],
    tn: ToInt[N]
  ): Arbitrary[F[T, Less[N]]] =
    rangeClosedOpenArbitrary(bounded.minValue, nt.fromInt(tn()))

  implicit def lessEqualArbitraryWit[F[_, _]: RefType, T: Numeric: Choose, N <: T](
    implicit
    bounded: Bounded[T],
    wn: Witness.Aux[N]
  ): Arbitrary[F[T, LessEqual[N]]] =
    rangeClosedArbitrary(bounded.minValue, wn.value)

  implicit def lessEqualArbitraryNat[F[_, _]: RefType, T: Choose, N <: Nat](
    implicit
    bounded: Bounded[T],
    nt: Numeric[T],
    tn: ToInt[N]
  ): Arbitrary[F[T, LessEqual[N]]] =
    rangeClosedArbitrary(bounded.minValue, nt.fromInt(tn()))

  implicit def greaterArbitraryWit[F[_, _]: RefType, T: Numeric: Choose, N <: T](
    implicit
    bounded: Bounded[T],
    wn: Witness.Aux[N]
  ): Arbitrary[F[T, Greater[N]]] =
    rangeOpenClosedArbitrary(wn.value, bounded.maxValue)

  implicit def greaterArbitraryNat[F[_, _]: RefType, T: Choose, N <: Nat](
    implicit
    bounded: Bounded[T],
    nt: Numeric[T],
    tn: ToInt[N]
  ): Arbitrary[F[T, Greater[N]]] =
    rangeOpenClosedArbitrary(nt.fromInt(tn()), bounded.maxValue)

  implicit def greaterEqualArbitraryWit[F[_, _]: RefType, T: Numeric: Choose, N <: T](
    implicit
    bounded: Bounded[T],
    wn: Witness.Aux[N]
  ): Arbitrary[F[T, GreaterEqual[N]]] =
    rangeClosedArbitrary(wn.value, bounded.maxValue)

  implicit def greaterEqualArbitraryNat[F[_, _]: RefType, T: Choose, N <: Nat](
    implicit
    bounded: Bounded[T],
    nt: Numeric[T],
    tn: ToInt[N]
  ): Arbitrary[F[T, GreaterEqual[N]]] =
    rangeClosedArbitrary(nt.fromInt(tn()), bounded.maxValue)

  ///

  implicit def intervalOpenArbitrary[F[_, _]: RefType, T: Numeric: Choose, L <: T, H <: T](
    implicit
    wl: Witness.Aux[L],
    wh: Witness.Aux[H]
  ): Arbitrary[F[T, Interval.Open[L, H]]] =
    rangeOpenArbitrary(wl.value, wh.value)

  implicit def intervalOpenClosedArbitrary[F[_, _]: RefType, T: Numeric: Choose, L <: T, H <: T](
    implicit
    wl: Witness.Aux[L],
    wh: Witness.Aux[H]
  ): Arbitrary[F[T, Interval.OpenClosed[L, H]]] =
    rangeOpenClosedArbitrary(wl.value, wh.value)

  implicit def intervalClosedOpenArbitrary[F[_, _]: RefType, T: Numeric: Choose, L <: T, H <: T](
    implicit
    wl: Witness.Aux[L],
    wh: Witness.Aux[H]
  ): Arbitrary[F[T, Interval.ClosedOpen[L, H]]] =
    rangeClosedOpenArbitrary(wl.value, wh.value)

  implicit def intervalClosedArbitrary[F[_, _]: RefType, T: Numeric: Choose, L <: T, H <: T](
    implicit
    wl: Witness.Aux[L],
    wh: Witness.Aux[H]
  ): Arbitrary[F[T, Interval.Closed[L, H]]] =
    rangeClosedArbitrary(wl.value, wh.value)

  ///

  private def rangeOpenArbitrary[F[_, _]: RefType, T: Choose, P](min: T, max: T)(
    implicit
    nt: Numeric[T]
  ): Arbitrary[F[T, P]] = {
    import nt.mkOrderingOps
    arbitraryRefType(Gen.chooseNum(min, max).filter(t => t > min && t < max))
  }

  private def rangeOpenClosedArbitrary[F[_, _]: RefType, T: Choose, P](min: T, max: T)(
    implicit
    nt: Numeric[T]
  ): Arbitrary[F[T, P]] = {
    import nt.mkOrderingOps
    arbitraryRefType(Gen.chooseNum(min, max).filter(_ > min))
  }

  private def rangeClosedOpenArbitrary[F[_, _]: RefType, T: Choose, P](min: T, max: T)(
    implicit
    nt: Numeric[T]
  ): Arbitrary[F[T, P]] = {
    import nt.mkOrderingOps
    arbitraryRefType(Gen.chooseNum(min, max).filter(_ < max))
  }

  private def rangeClosedArbitrary[F[_, _]: RefType, T: Numeric: Choose, P](min: T, max: T): Arbitrary[F[T, P]] =
    arbitraryRefType(Gen.chooseNum(min, max))
}
