package eu.timepit.refined

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.test.illTyped

class BigLiteralsSpec extends Properties("BigLiterals") {

  property("autoRefineV") = secure {
    val a: BigInt Refined Positive = BigInt(1)
    val b: BigInt Refined Positive = BigInt(0x7fffffffffffffffL)
    val c: BigInt Refined Positive = BigInt("1")
    val d: BigDecimal Refined Positive = BigDecimal(1.0)
    val e: BigDecimal Refined Positive = BigDecimal.exact(0.1)
    val f: BigDecimal Refined Positive = BigDecimal.valueOf(0.3)

    illTyped("val err: BigInt Refined Equal[W.`0`.T] = BigInt(\"0\")")
    illTyped("val err: BigInt Refined Equal[W.`1`.T] = BigInt(1)")
    illTyped("val err: BigDecimal Refined Equal[W.`0.0`.T] = BigDecimal(0.0)")
    illTyped("val err: BigDecimal Refined Equal[W.`0.0`.T] = BigDecimal.exact(\"0.0\")")

    illTyped("val err: BigInt Refined Positive = BigInt(0)", """Predicate failed: \(0 > 0\).""")
    illTyped("val err: BigInt Refined Positive = BigInt(a.value.toInt)",
             "compile-time refinement.*")
    illTyped("val err: BigInt Refined Positive = BigInt(\"0.1\")", "compile-time refinement.*")

    (a.value ?= BigInt(1)) &&
    (b.value ?= BigInt(Long.MaxValue)) &&
    (c.value ?= BigInt(1)) &&
    (d.value ?= BigDecimal(1.0)) &&
    (e.value ?= BigDecimal.exact(0.1)) &&
    (f.value ?= BigDecimal.valueOf(0.3))
  }

}
