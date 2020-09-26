package eu.timepit.refined.scalacheck

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.char._
import eu.timepit.refined.numeric.Interval
import eu.timepit.refined.scalacheck.boolean._
import eu.timepit.refined.scalacheck.char._
import eu.timepit.refined.scalacheck.numeric._
import eu.timepit.refined.types.char.{LowerCaseChar, UpperCaseChar}
import org.scalacheck.Properties

class CharArbitrarySpec extends Properties("CharArbitrary") {

  property("Digit") = checkArbitraryRefinedType[Char Refined Digit]

  property("Letter") = checkArbitraryRefinedType[Char Refined Letter]

  property("LowerCaseChar") = checkArbitraryRefinedType[LowerCaseChar]

  property("UpperCaseChar") = checkArbitraryRefinedType[UpperCaseChar]

  property("Whitespace") = checkArbitraryRefinedType[Char Refined Whitespace]

  property("LetterOrDigit") = checkArbitraryRefinedType[Char Refined LetterOrDigit]

  property("HexDigit") = {
    type HexDigit = Digit Or Interval.Closed[W.`'a'`.T, W.`'f'`.T]
    checkArbitraryRefinedType[Char Refined HexDigit]
  }
}
