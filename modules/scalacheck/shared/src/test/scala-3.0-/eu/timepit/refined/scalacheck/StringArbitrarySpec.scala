package eu.timepit.refined.scalacheck

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.{MaxSize, Size}
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.scalacheck.generic._
import eu.timepit.refined.scalacheck.numeric._
import eu.timepit.refined.scalacheck.string._
import eu.timepit.refined.string._
import eu.timepit.refined.types.string.{
  FiniteString,
  NonEmptyFiniteString,
  NonEmptyString,
  TrimmedString
}
import org.scalacheck.Properties

class StringArbitrarySpec extends Properties("StringArbitrary") {

  property("EndsWith[S]") = checkArbitraryRefinedType[String Refined EndsWith[W.`"abc"`.T]]

  property("StartsWith[S]") = checkArbitraryRefinedType[String Refined StartsWith[W.`"abc"`.T]]

  property("NonEmptyString") = checkArbitraryRefinedType[NonEmptyString]

  property("TrimmedString") = checkArbitraryRefinedType[TrimmedString]

  property("MaxSize[16]") = checkArbitraryRefinedType[String Refined MaxSize[W.`16`.T]]

  property("FiniteString[10]") = checkArbitraryRefinedType[FiniteString[W.`10`.T]]

  property("Size[Equal[8]]") = checkArbitraryRefinedType[String Refined Size[Equal[W.`8`.T]]]

  property("NonEmptyFiniteString[10]") = checkArbitraryRefinedType[NonEmptyFiniteString[W.`10`.T]]

  property("Uuid") = checkArbitraryRefinedType[String Refined Uuid]

  property("ValidByte") = checkArbitraryRefinedType[String Refined ValidByte]

  property("ValidShort") = checkArbitraryRefinedType[String Refined ValidShort]

  property("ValidInt") = checkArbitraryRefinedType[String Refined ValidInt]

  property("ValidLong") = checkArbitraryRefinedType[String Refined ValidLong]

  property("ValidFloat") = checkArbitraryRefinedType[String Refined ValidFloat]

  property("ValidDouble") = checkArbitraryRefinedType[String Refined ValidDouble]

  property("ValidBigInt") = checkArbitraryRefinedType[String Refined ValidBigInt]

  property("ValidBigDecimal") = checkArbitraryRefinedType[String Refined ValidBigDecimal]
}
