package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.numeric._
import org.scalacheck.{ Arbitrary, Gen }
import shapeless.Witness

object numericArbitrary {

  implicit def lessArbitrary[F[_, _], T, N <: T](
    implicit
    rt: RefType[F],
    wn: Witness.Aux[N],
    nt: Numeric[T],
    c: Gen.Choose[T]
  ): Arbitrary[F[T, Less[N]]] = {
    val gen = Gen.chooseNum(nt.fromInt(Int.MinValue), wn.value).filter(nt.lt(_, wn.value))
    arbitraryRefType(gen)
  }

  implicit def lessEqualArbitrary[F[_, _], T, N <: T](
    implicit
    rt: RefType[F],
    wn: Witness.Aux[N],
    nt: Numeric[T],
    c: Gen.Choose[T]
  ): Arbitrary[F[T, LessEqual[N]]] = {
    val gen = Gen.chooseNum(nt.fromInt(Int.MinValue), wn.value)
    arbitraryRefType(gen)
  }

  implicit def greaterArbitrary[F[_, _], T, N <: T](
    implicit
    rt: RefType[F],
    wn: Witness.Aux[N],
    nt: Numeric[T],
    c: Gen.Choose[T]
  ): Arbitrary[F[T, Greater[N]]] = {
    val gen = Gen.chooseNum(wn.value, nt.fromInt(Int.MaxValue)).filter(nt.gt(_, wn.value))
    arbitraryRefType(gen)
  }

  implicit def greaterEqualArbitrary[F[_, _], T, N <: T](
    implicit
    rt: RefType[F],
    wn: Witness.Aux[N],
    nt: Numeric[T],
    c: Gen.Choose[T]
  ): Arbitrary[F[T, GreaterEqual[N]]] = {
    val gen = Gen.chooseNum(wn.value, nt.fromInt(Int.MaxValue))
    arbitraryRefType(gen)
  }

  implicit def intervalArbitrary[F[_, _], T, L <: T, H <: T](
    implicit
    rt: RefType[F],
    wl: Witness.Aux[L],
    wh: Witness.Aux[H],
    nt: Numeric[T],
    c: Gen.Choose[T]
  ): Arbitrary[F[T, Interval[L, H]]] =
    arbitraryRefType(Gen.chooseNum(wl.value, wh.value))
}
