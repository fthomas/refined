package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.char._
import org.scalacheck.{Arbitrary, Gen}

/**
 * Module that provides `Arbitrary` instances for `Char` related
 * predicates.
 */
object char {

  implicit def digitArbitrary[F[_, _]: RefType]: Arbitrary[F[Char, Digit]] =
    arbitraryRefType(Gen.numChar)

  implicit def letterArbitrary[F[_, _]: RefType]: Arbitrary[F[Char, Letter]] =
    arbitraryRefType(Gen.alphaChar)

  implicit def lowerCaseArbitrary[F[_, _]: RefType]: Arbitrary[F[Char, LowerCase]] =
    arbitraryRefType(Gen.alphaLowerChar)

  implicit def upperCaseArbitrary[F[_, _]: RefType]: Arbitrary[F[Char, UpperCase]] =
    arbitraryRefType(Gen.alphaUpperChar)

  implicit def whitespaceArbitrary[F[_, _]: RefType]: Arbitrary[F[Char, Whitespace]] =
    arbitraryRefType(Gen.oneOf(whitespaceChars))

  private val whitespaceChars: Seq[Char] =
    (Char.MinValue to Char.MaxValue).filter(_.isWhitespace)
}
