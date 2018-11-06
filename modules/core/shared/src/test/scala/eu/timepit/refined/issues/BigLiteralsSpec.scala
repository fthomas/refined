package eu.timepit.refined.issues

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.test.illTyped

class BigLiteralsSpec extends Properties("BigLiterals") {

  property("autoRefineV") = secure {
    val ii: BigInt Refined Positive = BigInt(1)
    val il: BigInt Refined Positive = BigInt(0X7FFFFFFFFFFFFFFFL)
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

    illTyped("val err: BigInt Refined Equal[W.`0`.T] = BigInt(\"0\")")
    illTyped("val err: BigInt Refined Equal[W.`1`.T] = BigInt(1)")
    illTyped("val err: BigDecimal Refined Equal[W.`0.0`.T] = BigDecimal(0.0)")
    illTyped("val err: BigDecimal Refined Equal[W.`0.0`.T] = BigDecimal.exact(\"0.0\")")

    illTyped("val err: BigInt Refined Positive = BigInt(0)", """Predicate failed: \(0 > 0\).""")
    illTyped(
      "val err: BigInt Refined Positive = BigInt(ii.value.toInt)",
      "compile-time refinement.*"
    )
    illTyped("val err: BigInt Refined Positive = BigInt(\"0.1\")", "compile-time refinement.*")
    illTyped(
      "val err: BigInt Refined Positive = BigInt(java.math.BigInteger.ZERO)",
      "compile-time refinement.*"
    )
    illTyped(
      "val err: BigDecimal Refined Positive = BigDecimal(java.math.BigDecimal.ZERO)",
      "compile-time refinement.*"
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
