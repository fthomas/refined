package eu.timepit.refined

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.char.{Digit, Letter}
import eu.timepit.refined.generic._
import eu.timepit.refined.numeric.Positive
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

  property("#36") = secure {
    val a: BigInt Refined Positive = BigInt(1)
    val b: BigInt Refined Positive = BigInt(0x7fffffffffffffffL)
    val c: BigInt Refined Positive = BigInt("1")
    val d: BigDecimal Refined Positive = BigDecimal(1.0)
    val e: BigDecimal Refined Positive = BigDecimal.exact(0.1)
    val f: BigDecimal Refined Positive = BigDecimal.valueOf(0.1)

    val g: BigInt Refined Equal[W.`0`.T] = BigInt("0")
    val h: BigInt Refined Equal[W.`1`.T] = BigInt(1)
    val i: BigDecimal Refined Equal[W.`0.0`.T] = BigDecimal.exact("0.0")
    val j: BigDecimal Refined Equal[W.`0.0`.T] = autoRefineV(BigDecimal(0.0))

    illTyped("val err: BigInt Refined Positive = BigInt(0)", """Predicate failed: \(0 > 0\).""")
    illTyped("val err: BigInt Refined Positive = BigInt(a.value.toInt)", "compile-time refinement.*")
    illTyped("val err: BigInt Refined Positive = BigInt(\"0.1\")", "compile-time refinement.*")
  }

  property("#260") = secure {
    val somePosInt: Option[PosInt] = Some(5)
    somePosInt.isDefined
  }
}
