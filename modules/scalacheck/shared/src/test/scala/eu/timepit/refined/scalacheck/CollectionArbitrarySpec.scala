package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.{MaxSize, MinSize}
import eu.timepit.refined.scalacheck.numeric._
import eu.timepit.refined.scalacheck.collection._
import org.scalacheck.Properties
import shapeless.nat._

class CollectionArbitrarySpec extends Properties("CollectionArbitrary") {

  property("MaxSize") = checkArbitraryRefType[Refined, List[String], MaxSize[_10]]

  property("MinSize") = checkArbitraryRefType[Refined, List[String], MinSize[_10]]
}
