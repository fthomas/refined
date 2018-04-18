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

  property("List[String] Refined MaxSize[42]") =
    checkArbitraryRefinedType[List[String] Refined MaxSize[W.`42`.T]]

  property("List[String] Refined MaxSize[_13]") =
    checkArbitraryRefinedType[List[String] Refined MaxSize[_13]]

  property("Vector[Int] Refined MaxSize[23]") =
    checkArbitraryRefinedType[Vector[Int] Refined MaxSize[W.`23`.T]]

  property("Size[Interval.Closed[23, 42]]") =
    checkArbitraryRefinedType[List[Char] Refined Size[Interval.Closed[W.`23`.T, W.`42`.T]]]
}
