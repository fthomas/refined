package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.{RefType, Validate}
import org.scalacheck.{Arbitrary, Cogen, Gen, Prop}

object reftype extends RefTypeInstances

trait RefTypeInstances {

  def arbitraryRefType[F[_, _], T, P](gen: Gen[T])(implicit rt: RefType[F]): Arbitrary[F[T, P]] =
    Arbitrary(gen.map(rt.unsafeWrap))

  def checkArbitraryRefType[F[_, _], T, P](implicit arb: Arbitrary[F[T, P]],
                                           rt: RefType[F],
                                           v: Validate[T, P]): Prop =
    Prop.forAll((tp: F[T, P]) => v.isValid(rt.unwrap(tp)))

  implicit def refTypeCogen[F[_, _], T: Cogen, P](implicit rt: RefType[F]): Cogen[F[T, P]] =
    Cogen[T].contramap(tp => rt.unwrap(tp))
}
