package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.char._
import org.scalacheck.{ Arbitrary, Gen }

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
    arbitraryRefType(Gen.oneOf(
      // https://en.wikipedia.org/wiki/Whitespace_character
      '\u0009', '\u000A', '\u000B', '\u000C', '\u000D', '\u0020',
      '\u2000', '\u2001', '\u2002', '\u2003', '\u2004', '\u2005', '\u2006', '\u2008', '\u2009', '\u200A',
      '\u2028', '\u2029', '\u205F'
    ))
}
