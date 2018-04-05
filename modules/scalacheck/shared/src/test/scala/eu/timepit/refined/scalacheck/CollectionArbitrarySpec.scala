package eu.timepit.refined.scalacheck

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.{MaxSize, Size}
import eu.timepit.refined.numeric.Interval
import eu.timepit.refined.scalacheck.collection._
import eu.timepit.refined.scalacheck.numeric._
import org.scalacheck.Properties
import shapeless.nat._

class CollectionArbitrarySpec extends Properties("CollectionArbitrary") {

  property("List with MaxSize[42]") =
    checkArbitraryRefType[Refined, List[String], MaxSize[W.`42`.T]]

  property("Vector with MaxSize[23]") =
    checkArbitraryRefType[Refined, Vector[Int], MaxSize[W.`23`.T]]

  property("MaxSize.Nat") = checkArbitraryRefType[Refined, List[String], MaxSize[_13]]

  property("Size.Interval.Closed") =
    checkArbitraryRefType[Refined, List[String], Size[Interval.Closed[W.`23`.T, W.`42`.T]]]
}
