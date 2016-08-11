package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.char._
import eu.timepit.refined.numeric.Interval
import eu.timepit.refined.scalacheck.boolean._
import eu.timepit.refined.scalacheck.char._
import eu.timepit.refined.scalacheck.numeric._
import org.scalacheck.Properties

class CharArbitrarySpec extends Properties("CharArbitrarySpec") {

  property("Digit") =
    checkArbitraryRefinedType[Char Refined Digit]

  property("Letter") =
    checkArbitraryRefinedType[Char Refined Letter]

  property("LowerCase") =
    checkArbitraryRefinedType[Char Refined LowerCase]

  property("UpperCase") =
    checkArbitraryRefinedType[Char Refined UpperCase]

  property("Whitespace") =
    checkArbitraryRefinedType[Char Refined Whitespace]

  property("LetterOrDigit") =
    checkArbitraryRefinedType[Char Refined LetterOrDigit]

  property("HexDigit") = {
    type HexDigit = Digit Or Interval.Closed[W.`'a'`.T, W.`'f'`.T]
    checkArbitraryRefinedType[Char Refined HexDigit]
  }
}
