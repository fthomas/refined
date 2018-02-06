package eu.timepit.refined.types

import eu.timepit.refined.types.all._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class CharTypesSpec extends Properties("CharTypes") {

  property("LowerCaseChar.from('a')") = secure {
    LowerCaseChar.from('a').isRight
  }

  property("UpperCaseChar.from('A')") = secure {
    UpperCaseChar.from('A').isRight
  }
}
