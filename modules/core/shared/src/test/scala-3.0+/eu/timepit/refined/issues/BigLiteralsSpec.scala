package eu.timepit.refined.issues

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import eu.timepit.refined.test.ScalaVersionSpecific.illTyped

class BigLiteralsSpec extends Properties("BigLiterals") {

  property("autoRefineV") = secure {
    val ii: BigInt Refined Positive = BigInt(1)
    val il: BigInt Refined Positive = BigInt(0x7fffffffffffffffL)
    val is: BigInt Refined Positive = BigInt("1")

    val dd: BigDecimal Refined Positive = BigDecimal(1.0)
    val di: BigDecimal Refined Positive = BigDecimal(1)
    val dl: BigDecimal Refined Positive = BigDecimal(1L)
    val ds: BigDecimal Refined Positive = BigDecimal("1.0")

    val ded: BigDecimal Refined Positive = BigDecimal.exact(0.1)
    val dei: BigDecimal Refined Positive = BigDecimal.exact(1)
    val del: BigDecimal Refined Positive = BigDecimal.exact(1L)
    val des: BigDecimal Refined Positive = BigDecimal.exact("0.1")

    val dvd: BigDecimal Refined Positive = BigDecimal.valueOf(0.3)
    val dvl: BigDecimal Refined Positive = BigDecimal.valueOf(1L)

    illTyped("val err: BigInt Refined Equal[0] = BigInt(\"0\")")
    illTyped("val err: BigInt Refined Equal[1] = BigInt(1)")
    illTyped("val err: BigDecimal Refined Equal[0.0] = BigDecimal(0.0)")
    illTyped("val err: BigDecimal Refined Equal[0.0] = BigDecimal.exact(\"0.0\")")

    // These fail via the rejected `autoRefineV` conversion, which surfaces as a type mismatch against
    // the required refined type (rather than the Scala 2 "Predicate failed" / "compile-time refinement"
    // macro messages). The non-constant case uses a self-contained `def` (no block-local `ii`).
    illTyped("val err: BigInt Refined Positive = BigInt(0)", "Required: BigInt Refined .*Positive")
    illTyped(
      "{ def n: Int = 0; val err: BigInt Refined Positive = BigInt(n) }",
      "Required: BigInt Refined .*Positive"
    )
    illTyped(
      "val err: BigInt Refined Positive = BigInt(\"0.1\")",
      "Required: BigInt Refined .*Positive"
    )
    illTyped(
      "val err: BigInt Refined Positive = BigInt(java.math.BigInteger.ZERO)",
      "Required: BigInt Refined .*Positive"
    )
    illTyped(
      "val err: BigDecimal Refined Positive = BigDecimal(java.math.BigDecimal.ZERO)",
      "Required: BigDecimal Refined .*Positive"
    )

    (ii.value ?= BigInt(1)) &&
    (il.value ?= BigInt(Long.MaxValue)) &&
    (is.value ?= BigInt(1)) &&
    (dd.value ?= BigDecimal(1.0)) &&
    (di.value ?= BigDecimal(1)) &&
    (dl.value ?= BigDecimal(1L)) &&
    (ds.value ?= BigDecimal("1.0")) &&
    (ded.value ?= BigDecimal.exact(0.1)) &&
    (dei.value ?= BigDecimal.exact(1)) &&
    (del.value ?= BigDecimal.exact(1L)) &&
    (des.value ?= BigDecimal.exact("0.1")) &&
    (dvd.value ?= BigDecimal.valueOf(0.3)) &&
    (dvl.value ?= BigDecimal.valueOf(1L))
  }

}
