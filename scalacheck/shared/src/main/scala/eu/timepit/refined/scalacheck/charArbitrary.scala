package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.char.{ Digit, Letter, LowerCase, UpperCase }
import org.scalacheck.{ Arbitrary, Gen }

object charArbitrary {

  implicit def digitArbitrary[F[_, _]: RefType]: Arbitrary[F[Char, Digit]] =
    arbitraryRefType(Gen.numChar)

  implicit def letterArbitrary[F[_, _]: RefType]: Arbitrary[F[Char, Letter]] =
    arbitraryRefType(Gen.alphaChar)

  implicit def lowerCaseArbitrary[F[_, _]: RefType]: Arbitrary[F[Char, LowerCase]] =
    arbitraryRefType(Gen.alphaLowerChar)

  implicit def upperCaseArbitrary[F[_, _]: RefType]: Arbitrary[F[Char, UpperCase]] =
    arbitraryRefType(Gen.alphaUpperChar)
}
