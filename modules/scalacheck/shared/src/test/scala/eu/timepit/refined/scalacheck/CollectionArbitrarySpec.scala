package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.MaxSize
import eu.timepit.refined.scalacheck.collection._
import org.scalacheck.Properties
import shapeless.nat._

class CollectionArbitrarySpec extends Properties("CollectionArbitrary") {

  property("MaxSize.Nat") = checkArbitraryRefType[Refined, List[String], MaxSize[_10]]

}
