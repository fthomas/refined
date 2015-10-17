package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.char.{ Digit, Letter, LowerCase, UpperCase }
import org.scalacheck.{ Arbitrary, Gen }

object charArbitrary {

  implicit def digitArbitrary[F[_, _]](implicit rt: RefType[F]): Arbitrary[F[Char, Digit]] =
    Arbitrary(Gen.numChar.map(rt.unsafeWrap))

  implicit def letterArbitrary[F[_, _]](implicit rt: RefType[F]): Arbitrary[F[Char, Letter]] =
    Arbitrary(Gen.alphaChar.map(rt.unsafeWrap))

  implicit def lowerCaseArbitrary[F[_, _]](implicit rt: RefType[F]): Arbitrary[F[Char, LowerCase]] =
    Arbitrary(Gen.alphaLowerChar.map(rt.unsafeWrap))

  implicit def upperCaseArbitrary[F[_, _]](implicit rt: RefType[F]): Arbitrary[F[Char, UpperCase]] =
    Arbitrary(Gen.alphaUpperChar.map(rt.unsafeWrap))
}
