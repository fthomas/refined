package eu.timepit.refined

import eu.timepit.refined.api.{Implies, Refined}
import eu.timepit.refined.auto._
import eu.timepit.refined.char.{Digit, Letter}
import eu.timepit.refined.generic._
import eu.timepit.refined.numeric.{Greater}
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.tag.@@
import shapeless.test.illTyped

class AutoSpec extends Properties("auto") {

  property("autoInfer") = secure {
    val a: Char Refined Equal[W.`'0'`.T] = '0'
    val b: Char Refined Digit = a
    illTyped(
      "val c: Char Refined Letter = a",
      """type mismatch \(invalid inference\):\s*eu.timepit.refined.generic.Equal\[Char\('0'\)\] does not imply\s*eu.timepit.refined.char.Letter"""
    )
    a == b
  }

  property("autoImply") = secure {
    val t = implicitly[Implies[Greater[W.`1`.T], Greater[W.`0`.T]]]
    illTyped(
      "implicitly[Implies[Greater[W.`-1`.T], Greater[W.`0`.T]]]",
      """type mismatch \(invalid inference\):\s*eu.timepit.refined.numeric.Greater\[Int\(-1\)\] does not imply\s*eu.timepit.refined.numeric.Greater\[Int\(0\)\]"""
    )
    t.show == "greaterInference(1, 0)"
  }

  property("autoUnwrap: PosInt: Int") = secure {
    val a: PosInt = PosInt.unsafeFrom(1)
    val b: Int = a
    a.value == b
  }

  property("autoUnwrap: PosInt + PosInt") = secure {
    val a = PosInt.unsafeFrom(1)
    val b = PosInt.unsafeFrom(2)
    (a + b) == 3
  }

  property("autoRefineV") = secure {
    val a: Char Refined Equal[W.`'0'`.T] = '0'
    illTyped("val b: Char Refined Equal[W.`'0'`.T] = '1'", """Predicate failed: \(1 == 0\).""")
    a.value == '0'
  }

  property("autoRefineT") = secure {
    val a: Char @@ Equal[W.`'0'`.T] = '0'
    illTyped("val b: Char @@ Equal[W.`'0'`.T] = '1'", """Predicate failed: \(1 == 0\).""")
    a == '0'
  }

  property("#260") = secure {
    val somePosInt: Option[PosInt] = Some(5)
    somePosInt.isDefined
  }
}
