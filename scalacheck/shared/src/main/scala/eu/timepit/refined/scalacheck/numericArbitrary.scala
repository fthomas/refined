package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.numeric.{ Greater, Interval, Less }
import org.scalacheck.{ Arbitrary, Gen }
import shapeless.Witness

object numericArbitrary {

  implicit def arbLess[F[_, _], T, N <: T](
    implicit
    rt: RefType[F],
    wn: Witness.Aux[N],
    nt: Numeric[T], c: Gen.Choose[T]
  ): Arbitrary[F[T, Less[N]]] = {
    val gen = Gen.chooseNum(nt.fromInt(Int.MinValue), nt.minus(wn.value, nt.one))
    Arbitrary(gen.map(rt.unsafeWrap))
  }

  implicit def arbGreater[F[_, _], T, N <: T](
    implicit
    rt: RefType[F],
    wn: Witness.Aux[N],
    nt: Numeric[T],
    c: Gen.Choose[T]
  ): Arbitrary[F[T, Greater[N]]] = {
    val gen = Gen.chooseNum(nt.plus(wn.value, nt.one), nt.fromInt(Int.MaxValue))
    Arbitrary(gen.map(rt.unsafeWrap))
  }

  implicit def arbInterval[F[_, _], T, L <: T, H <: T](
    implicit
    rt: RefType[F],
    wl: Witness.Aux[L],
    wh: Witness.Aux[H],
    nt: Numeric[T],
    c: Gen.Choose[T]
  ): Arbitrary[F[T, Interval[L, H]]] =
    Arbitrary(Gen.chooseNum(wl.value, wh.value).map(rt.unsafeWrap))
}
