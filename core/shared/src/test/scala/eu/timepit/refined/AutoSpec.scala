package eu.timepit.refined

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.char.{Digit, Letter}
import eu.timepit.refined.generic.Equal
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

  property("autoUnwrap") = secure {
    val a: Char Refined Letter = 'A'
    val b: Char = a
    a.value == b
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
}
