package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.char.{ Digit, Letter }
import eu.timepit.refined.scalacheck.charArbitrary._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class CharArbitrarySpec extends Properties("CharArbitrarySpec") {

  property("Digit") = forAll { (c: Char Refined Digit) =>
    c.get.isDigit
  }

  property("Letter") = forAll { (c: Char Refined Letter) =>
    c.get.isLetter
  }
}
