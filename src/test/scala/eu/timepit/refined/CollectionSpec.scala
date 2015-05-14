package eu.timepit.refined

import eu.timepit.refined.collection._
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class CollectionSpec extends Properties("collection") {
  property("Exists[Equal[_]].isValid") = forAll { (l: List[Int]) =>
    Predicate[Exists[Equal[_1]], List[Int]].isValid(l) ?= l.contains(1)
  }

  property("Exists[Equal[_]].show") = secure {
    Predicate[Exists[Equal[_1]], List[Int]].show(List(1, 2, 3)) ?=
      "!(!(1 == 1) && !(2 == 1) && !(3 == 1))"
  }

  property("Size[Greater[_]].isValid") = forAll { (l: List[Int]) =>
    Predicate[Size[Greater[_5]], List[Int]].isValid(l) ?= (l.size > 5)
  }
}
