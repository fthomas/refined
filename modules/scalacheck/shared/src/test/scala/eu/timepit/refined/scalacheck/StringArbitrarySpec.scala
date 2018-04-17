package eu.timepit.refined.scalacheck

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.{MaxSize, NonEmpty, Size}
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.scalacheck.generic._
import eu.timepit.refined.scalacheck.numeric._
import eu.timepit.refined.scalacheck.string._
import eu.timepit.refined.string._
import eu.timepit.refined.types.string.FiniteString
import org.scalacheck.Properties

class StringArbitrarySpec extends Properties("StringArbitrary") {

  property("EndsWith") = checkArbitraryRefType[Refined, String, EndsWith[W.`"abc"`.T]]

  property("StartsWith") = checkArbitraryRefType[Refined, String, StartsWith[W.`"abc"`.T]]

  property("NonEmpty") = checkArbitraryRefType[Refined, String, NonEmpty]

  property("MaxSize") = checkArbitraryRefType[Refined, String, MaxSize[W.`16`.T]]

  property("FiniteString[N]") = checkArbitraryRefinedType[FiniteString[W.`10`.T]]

  property("Size[Equal]") = checkArbitraryRefType[Refined, String, Size[Equal[W.`8`.T]]]
}
