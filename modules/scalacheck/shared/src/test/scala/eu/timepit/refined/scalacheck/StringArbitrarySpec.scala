package eu.timepit.refined.scalacheck

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.{MaxSize, Size}
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.scalacheck.generic._
import eu.timepit.refined.scalacheck.numeric._
import eu.timepit.refined.scalacheck.string._
import eu.timepit.refined.string._
import eu.timepit.refined.types.string.{FiniteString, NonEmptyString}
import org.scalacheck.Properties

class StringArbitrarySpec extends Properties("StringArbitrary") {

  property("EndsWith[S]") = checkArbitraryRefinedType[String Refined EndsWith[W.`"abc"`.T]]

  property("StartsWith[S]") = checkArbitraryRefinedType[String Refined StartsWith[W.`"abc"`.T]]

  property("NonEmptyString") = checkArbitraryRefinedType[NonEmptyString]

  property("MaxSize[16]") = checkArbitraryRefinedType[String Refined MaxSize[W.`16`.T]]

  property("FiniteString[10]") = checkArbitraryRefinedType[FiniteString[W.`10`.T]]

  property("Size[Equal[8]]") = checkArbitraryRefinedType[String Refined Size[Equal[W.`8`.T]]]
}
