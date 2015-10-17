package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.char._
import eu.timepit.refined.scalacheck.TestUtils._
import eu.timepit.refined.scalacheck.booleanArbitrary._
import eu.timepit.refined.scalacheck.charArbitrary._
import org.scalacheck.Properties

class CharArbitrarySpec extends Properties("CharArbitrarySpec") {

  property("Digit") = checkArbitrary[Refined, Char, Digit]

  property("Letter") = checkArbitrary[Refined, Char, Letter]

  property("LowerCase") = checkArbitrary[Refined, Char, LowerCase]

  property("UpperCase") = checkArbitrary[Refined, Char, UpperCase]

  property("LetterOrDigit") = checkArbitrary[Refined, Char, LetterOrDigit]
}
