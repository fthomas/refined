package eu.timepit.refined

import eu.timepit.refined.char._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class CharPredicateSpecJvm extends Properties("CharPredicate") {

  property("Letter.isValid") = forAll { (c: Char) =>
    Predicate[Letter, Char].isValid(c) ?= c.isLetter
  }

  property("Letter.show") = secure {
    Predicate[Letter, Char].show('c') ?= "isLetter('c')"
  }
}
