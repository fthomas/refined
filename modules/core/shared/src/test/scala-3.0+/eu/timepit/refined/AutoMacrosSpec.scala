package eu.timepit.refined

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.char.{Digit, Letter}
import eu.timepit.refined.generic._
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties
import eu.timepit.refined.test.ScalaVersionSpecific.illTyped

class AutoMacrosSpec extends Properties("auto.macros") {

  property("autoInfer") = secure {
    val a: Char Refined Equal['0'] = '0'
    val b: Char Refined Digit = a
    // Self-contained snippet that stays in auto mode: the inner `val` is built by `autoRefineV`, and
    // the missing `Inference[Equal['0'], Letter]` makes the `autoInfer` conversion on the outer `val`
    // inapplicable, surfacing as a type mismatch against the required type. (The source value is
    // defined inside the snippet block because `typeCheckErrors` cannot see the enclosing `a`.)
    illTyped(
      "{ val a0: Char Refined Equal['0'] = '0'; val c: Char Refined Letter = a0 }",
      "Required: Char Refined .*Letter"
    )
    a == b
  }

  property("autoRefineV") = secure {
    val a: Char Refined Equal['0'] = '0'
    illTyped("val b: Char Refined Equal['0'] = '1'", "Required: Char Refined .*Equal")
    a.value == '0'
  }

  property("#260") = secure {
    val somePosInt: Option[PosInt] = Some(5)
    somePosInt.isDefined
  }
}
