package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.char._
import eu.timepit.refined.scalacheck.boolean._
import eu.timepit.refined.scalacheck.char._
import org.scalacheck.Properties

class CharArbitrarySpec extends Properties("CharArbitrarySpec") {

  property("Digit") =
    checkArbitraryRefType[Refined, Char, Digit]

  property("Letter") =
    checkArbitraryRefType[Refined, Char, Letter]

  property("LowerCase") =
    checkArbitraryRefType[Refined, Char, LowerCase]

  property("UpperCase") =
    checkArbitraryRefType[Refined, Char, UpperCase]

  property("Whitespace") =
    checkArbitraryRefType[Refined, Char, Whitespace]

  property("LetterOrDigit") =
    checkArbitraryRefType[Refined, Char, LetterOrDigit]
}
