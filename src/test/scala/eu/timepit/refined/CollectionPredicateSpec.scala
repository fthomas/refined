package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.char.{ Digit, LowerCase }
import eu.timepit.refined.collection._
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class CollectionPredicateSpec extends Properties("CollectionPredicate") {

  property("Contains[W.`0`.T].isValid") = forAll { (l: List[Int]) =>
    Predicate[Contains[W.`0`.T], List[Int]].isValid(l) ?= l.contains(0)
  }

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

  property("Count[LowerCase, Greater[_2]].consistent") = forAll {
    consistent(Predicate[Count[LowerCase, Greater[_2]], List[Char]])
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

  property("Index[W.`2`.T, Digit].isValid") = forAll { (l: List[Char]) =>
    Predicate[Index[W.`2`.T, Digit], List[Char]].isValid(l) ?=
      l.lift(2).fold(false)(_.isDigit)
  }

  property("Index[W.`2`.T, Digit].validated") = secure {
    Predicate[Index[W.`2`.T, Digit], List[Char]].validated(List('a', 'b', 'c')) ?=
      Some("Predicate taking index(List(a, b, c), 2) = c failed: Predicate failed: isDigit('c').")
  }

  property("Index[W.`2`.T, Digit].validated") = secure {
    Predicate[Index[W.`2`.T, Digit], List[Char]].validated(List.empty) ?=
      Some("Predicate failed: empty collection.")
  }

  property("Last[Greater[_5]].isValid") = forAll { (l: List[Int]) =>
    Predicate[Last[Greater[_5]], List[Int]].isValid(l) ?= l.lastOption.fold(false)(_ > 5)
  }

  property("Last[Greater[_5]].show") = secure {
    Predicate[Last[Greater[_5]], List[Int]].show(List(1, 2, 3)) ?= "(3 > 5)"
  }

  property("Last[Greater[_5]].validated") = secure {
    Predicate[Last[Greater[_5]], List[Int]].validated(List(1, 2, 3)) ?=
      Some("Predicate taking last(List(1, 2, 3)) = 3 failed: Predicate failed: (3 > 5).")
  }

  property("Size[Greater[_]].isValid") = forAll { (l: List[Int]) =>
    Predicate[Size[Greater[_5]], List[Int]].isValid(l) ?= (l.size > 5)
  }

  property("Size[Greater[_]].consistent") = forAll {
    consistent(Predicate[Size[Greater[_5]], List[Int]])
  }
}
