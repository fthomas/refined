package eu.timepit.refined.types

import eu.timepit.refined.W
import eu.timepit.refined.types.string.FiniteString
import org.scalacheck.Prop._
import org.scalacheck.Properties

class StringTypesSpec extends Properties("StringTypes") {
  final val FString3 = FiniteString[W.`3`.T]

  property("""FString3.from("")""") = secure {
    val str = ""
    FString3.from(str).right.map(_.value) ?= Right(str)
  }

  property("""FString3.from("abc")""") = secure {
    val str = "abc"
    FString3.from(str).right.map(_.value) ?= Right(str)
  }

  property("""FString3.from("abcd")""") = secure {
    val str = "abcd"
    FString3.from(str) ?= Left(
      "Predicate taking size(abcd) = 4 failed: Predicate (4 > 3) did not fail.")
  }
}
