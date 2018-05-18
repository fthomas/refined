package eu.timepit.refined.types

import eu.timepit.refined.types.all._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class CharTypesSpec extends Properties("CharTypes") {

  property("LowerCaseChar.from('a')") = secure {
    LowerCaseChar.from('a').isRight
  }

  property("LowerCaseChar.from('A')") = secure {
    LowerCaseChar.from('A') ?= Left("Predicate failed: isLower('A').")
  }

  property("UpperCaseChar.from('A')") = secure {
    UpperCaseChar.from('A').isRight
  }

  property("UpperCaseChar.from('a')") = secure {
    UpperCaseChar.from('a') ?= Left("Predicate failed: isUpper('a').")
  }
}
