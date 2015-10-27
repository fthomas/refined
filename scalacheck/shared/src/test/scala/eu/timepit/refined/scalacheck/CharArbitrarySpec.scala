package eu.timepit.refined
package scalacheck

import eu.timepit.refined.char._
import eu.timepit.refined.scalacheck.TestUtils._
import eu.timepit.refined.scalacheck.boolean._
import eu.timepit.refined.scalacheck.char._
import org.scalacheck.Properties

class CharArbitrarySpec extends Properties("CharArbitrarySpec") {

  property("Digit") = checkArbitrary[Char, Digit]

  property("Letter") = checkArbitrary[Char, Letter]

  property("LowerCase") = checkArbitrary[Char, LowerCase]

  property("UpperCase") = checkArbitrary[Char, UpperCase]

  property("Whitespace") = checkArbitrary[Char, Whitespace]

  property("LetterOrDigit") = checkArbitrary[Char, LetterOrDigit]
}
