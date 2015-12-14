package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.numeric._
import eu.timepit.refined.boolean.Not
import org.scalacheck.Gen.Choose
import org.scalacheck.{ Arbitrary, Gen }
import shapeless.ops.nat.ToInt
import shapeless.{ Nat, Witness }

object numeric {

  trait Bounded[T] extends {
    def minValue: T
    def maxValue: T
  }

  object Bounded {
    def apply[T](min: T, max: T): Bounded[T] = new Bounded[T] {
      val minValue = min
      val maxValue = max
    }
    implicit val int: Bounded[Int] = Bounded(Int.MinValue, Int.MaxValue)
    implicit val short: Bounded[Short] = Bounded(Short.MinValue, Short.MaxValue)
    implicit val long: Bounded[Long] = Bounded(Long.MinValue, Long.MaxValue)
  }

  implicit def lessArbitrary[F[_, _]: RefType, T: Choose: Numeric: Bounded, N <: T](implicit wn: Witness.Aux[N]): Arbitrary[F[T, Less[N]]] =
    lessArbitraryImpl(wn.value)

  implicit def lessArbitraryNat[F[_, _]: RefType, T: Choose: Bounded, N <: Nat: ToInt](implicit num: Numeric[T]): Arbitrary[F[T, Less[N]]] =
    lessArbitraryImpl(num.fromInt(Nat.toInt[N]))

  private def lessArbitraryImpl[F[_, _]: RefType, T: Choose, N](value: T)(
    implicit
    num: Numeric[T],
    bound: Bounded[T]
  ): Arbitrary[F[T, Less[N]]] = {
    val gen = Gen.chooseNum(bound.minValue, value).filter(!num.equiv(_, value))
    arbitraryRefType(gen)
  }

  implicit def lessEqualArbitrary[F[_, _]: RefType, T: Choose, N <: T](
    implicit
    wn: Witness.Aux[N],
    nt: Numeric[T],
    bound: Bounded[T]
  ): Arbitrary[F[T, LessEqual[N]]] = {
    val gen = Gen.chooseNum(bound.minValue, wn.value)
    arbitraryRefType(gen)
  }

  implicit def lessEqualArbitraryNat[F[_, _]: RefType, T: Choose, N <: Nat: ToInt](
    implicit
    num: Numeric[T],
    bound: Bounded[T]
  ): Arbitrary[F[T, LessEqual[N]]] = {
    val gen = Gen.chooseNum(bound.minValue, num.fromInt(Nat.toInt[N]))
    arbitraryRefType(gen)
  }

  implicit def greaterArbitrary[F[_, _]: RefType, T: Choose: Numeric, N <: T](
    implicit
    wn: Witness.Aux[N],
    bound: Bounded[T]
  ): Arbitrary[F[T, Greater[N]]] =
    greaterArbitraryImpl(min = wn.value, max = bound.maxValue)

  implicit def greaterArbitraryNat[F[_, _]: RefType, T: Choose, N <: Nat: ToInt](
    implicit
    num: Numeric[T],
    bound: Bounded[T]
  ): Arbitrary[F[T, Greater[N]]] =
    greaterArbitraryImpl(min = num.fromInt(Nat.toInt[N]), max = bound.maxValue)

  private def greaterArbitraryImpl[F[_, _]: RefType, T: Choose, N](min: T, max: T)(
    implicit
    num: Numeric[T]
  ): Arbitrary[F[T, Greater[N]]] = {
    val gen = Gen.chooseNum(min, max).filter(!num.equiv(_, min))
    arbitraryRefType(gen)
  }

  def greaterArbitraryRange[F[_, _], T: Choose: Numeric, N <: Nat](min: F[T, Greater[N]], max: F[T, Greater[N]])(
    implicit
    rt: RefType[F]
  ): Arbitrary[F[T, Greater[N]]] =
    greaterArbitraryImpl(min = rt.unwrap(min), max = rt.unwrap(max))

  def greaterArbitraryMax[F[_, _], T: Choose, N <: Nat: ToInt](max: F[T, Greater[N]])(
    implicit
    rt: RefType[F],
    num: Numeric[T]
  ): Arbitrary[F[T, Greater[N]]] =
    greaterArbitraryImpl(min = num.fromInt(Nat.toInt[N]), max = rt.unwrap(max))

  implicit def greaterEqualArbitrary[F[_, _]: RefType, T: Choose, N <: T](
    implicit
    wn: Witness.Aux[N],
    nt: Numeric[T],
    bound: Bounded[T]
  ): Arbitrary[F[T, GreaterEqual[N]]] = {
    val gen = Gen.chooseNum(wn.value, bound.maxValue)
    arbitraryRefType(gen)
  }

  implicit def intervalOpenArbitrary[F[_, _]: RefType, T: Choose, L <: T, H <: T](
    implicit
    wl: Witness.Aux[L],
    wh: Witness.Aux[H],
    nt: Numeric[T]
  ): Arbitrary[F[T, Interval.Open[L, H]]] = {
    val gen = Gen.chooseNum(wl.value, wh.value).filter(t => nt.gt(t, wl.value) && nt.lt(t, wh.value))
    arbitraryRefType(gen)
  }

  implicit def intervalOpenClosedArbitrary[F[_, _]: RefType, T: Choose, L <: T, H <: T](
    implicit
    wl: Witness.Aux[L],
    wh: Witness.Aux[H],
    nt: Numeric[T]
  ): Arbitrary[F[T, Interval.OpenClosed[L, H]]] = {
    val gen = Gen.chooseNum(wl.value, wh.value).filter(t => nt.gt(t, wl.value))
    arbitraryRefType(gen)
  }

  implicit def intervalClosedOpenArbitrary[F[_, _]: RefType, T: Choose, L <: T, H <: T](
    implicit
    wl: Witness.Aux[L],
    wh: Witness.Aux[H],
    nt: Numeric[T]
  ): Arbitrary[F[T, Interval.ClosedOpen[L, H]]] = {
    val gen = Gen.chooseNum(wl.value, wh.value).filter(t => nt.lt(t, wh.value))
    arbitraryRefType(gen)
  }

  implicit def intervalClosedArbitrary[F[_, _]: RefType, T: Choose, L <: T, H <: T](
    implicit
    wl: Witness.Aux[L],
    wh: Witness.Aux[H],
    nt: Numeric[T]
  ): Arbitrary[F[T, Interval.Closed[L, H]]] = {
    val gen = Gen.chooseNum(wl.value, wh.value)
    arbitraryRefType(gen)
  }

  implicit def notLessArbitraryNat[F[_, _]: RefType, T: Choose, N <: Nat: ToInt](
    implicit
    num: Numeric[T],
    bound: Bounded[T]
  ): Arbitrary[F[T, Not[Less[N]]]] = {
    val gen = Gen.chooseNum(num.fromInt(Nat.toInt[N]), bound.maxValue)
    arbitraryRefType(gen)
  }

  def notLessArbitraryRange[F[_, _], T: Choose: Numeric, N <: Nat: ToInt](min: F[T, Not[Less[N]]], max: F[T, Not[Less[N]]])(
    implicit
    rt: RefType[F]
  ): Arbitrary[F[T, Not[Less[N]]]] = {
    val gen = Gen.chooseNum(rt.unwrap(min), rt.unwrap(max))
    arbitraryRefType(gen)
  }

  def notLessArbitraryMax[F[_, _], T: Choose, N <: Nat: ToInt](max: F[T, Not[Less[N]]])(
    implicit
    rt: RefType[F],
    num: Numeric[T]
  ): Arbitrary[F[T, Not[Less[N]]]] = {
    val gen = Gen.chooseNum(num.fromInt(Nat.toInt[N]), rt.unwrap(max))
    arbitraryRefType(gen)
  }
}
