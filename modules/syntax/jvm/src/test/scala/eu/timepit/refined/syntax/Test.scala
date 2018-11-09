package syntax.test

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.syntax._
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.{NonEmptyString, TrimmedString}
import org.scalacheck.Properties
import shapeless.test.illTyped

object SyntaxSpec extends Properties("syntax") {
  val nes: NonEmptyString = NonEmptyString.unsafeFrom("Hello World")

  property("size") = wellTyped {
    val s: PosInt = nes.size
  }

  property("+") = wellTyped {
    val nes2: NonEmptyString = nes + "!"
  }

  property("*") = wellTyped {
    val nes2: NonEmptyString = nes * 3
  }

  property("trim") = wellTyped {
    val trimmed: TrimmedString = nes.trim
  }

  property("interpolator") = wellTyped {
    val nes2: NonEmptyString = nes"Hello World"
    illTyped("""val nes3: NonEmptyString = nes""""")
  }
}
