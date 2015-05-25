package eu.timepit.refined

import eu.timepit.refined.char.LowerCase
import eu.timepit.refined.collection._
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class CollectionSpec extends Properties("collection") {
  property("Count[LowerCase, Greater[_2]].isValid") = forAll { (l: List[Char]) =>
    Predicate[Count[LowerCase, Greater[_2]], List[Char]].isValid(l) ?= (l.count(_.isLower) > 2)
  }

  property("Count[LowerCase, Greater[_2]].show") = secure {
    Predicate[Count[LowerCase, Greater[_2]], List[Char]].show(List('a', 'B')) ?= "(1 > 2)"
  }

  property("Count[LowerCase, Greater[_2]].validated") = secure {
    Predicate[Count[LowerCase, Greater[_2]], List[Char]].validated(List('a', 'B')) ?=
      Some("Predicate taking count(isLower('a'), isLower('B')) = 1 failed: Predicate failed: (1 > 2).")
  }

  property("Empty.isValid") = forAll { (l: List[Int]) =>
    Predicate[Empty, List[Int]].isValid(l) ?= l.isEmpty
  }

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
