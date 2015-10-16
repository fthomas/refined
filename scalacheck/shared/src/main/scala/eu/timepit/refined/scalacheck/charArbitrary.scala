package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.char.{ Digit, Letter }
import org.scalacheck.{ Arbitrary, Gen }

object charArbitrary {

  implicit def digitArbitrary[F[_, _]](implicit rt: RefType[F]): Arbitrary[F[Char, Digit]] =
    Arbitrary(Gen.numChar.map(rt.unsafeWrap))

  implicit def letterArbitrary[F[_, _]](implicit rt: RefType[F]): Arbitrary[F[Char, Letter]] =
    Arbitrary(Gen.alphaChar.map(rt.unsafeWrap))
}
