package eu.timepit.refined.scalacheck

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.char._
import eu.timepit.refined.numeric._
import eu.timepit.refined.scalacheck.boolean._
import eu.timepit.refined.scalacheck.char._
import eu.timepit.refined.scalacheck.numeric._
import org.scalacheck.Properties

class CharArbitrarySpec extends Properties("CharArbitrarySpec") {

  property("Digit") = checkArbitraryRefType[Refined, Char, Digit]

  property("Letter") = checkArbitraryRefType[Refined, Char, Letter]

  property("LowerCase") = checkArbitraryRefType[Refined, Char, LowerCase]

  property("UpperCase") = checkArbitraryRefType[Refined, Char, UpperCase]

  property("Whitespace") = checkArbitraryRefType[Refined, Char, Whitespace]

  property("LetterOrDigit") = checkArbitraryRefType[Refined, Char, LetterOrDigit]

  property("HexDigit") = {
    type HexDigit = Digit Or Interval.Closed[W.`'a'`.T, W.`'f'`.T]
    checkArbitraryRefType[Refined, Char, HexDigit]
  }
}
