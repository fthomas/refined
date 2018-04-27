package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.boolean.And
import eu.timepit.refined.char.{Digit, LowerCase}
import eu.timepit.refined.collection._
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class CollectionValidateSpec extends Properties("CollectionValidate") {

  property("Contains.isValid") = forAll { (l: List[Int]) =>
    isValid[Contains[0]](l) ?= l.contains(0)
  }

  property("Contains.showExpr") = secure {
    showExpr[Contains[0]](List(1, 2, 3)) ?= "!(!(1 == 0) && !(2 == 0) && !(3 == 0))"
  }

  property("Contains.String.isValid") = forAll { (s: String) =>
    isValid[Contains['0']](s) ?= s.contains('0')
  }

  property("Contains.String.showExpr") = secure {
    showExpr[Contains['0']]("012") ?= "!(!(0 == 0) && !(1 == 0) && !(2 == 0))"
  }

  property("Count.isValid") = forAll { (l: List[Char]) =>
    isValid[Count[LowerCase, Greater[_2]]](l) ?= (l.count(_.isLower) > 2)
  }

  property("Count.showExpr") = secure {
    showExpr[Count[LowerCase, Greater[_2]]](List('a', 'B')) ?= "(1 > 2)"
  }

  property("Count.showResult") = secure {
    showResult[Count[LowerCase, Greater[_2]]](List('a', 'B')) ?=
      "Predicate taking count(isLower('a'), isLower('B')) = 1 failed: Predicate failed: (1 > 2)."
  }

  property("Count.String.isValid") = forAll { (s: String) =>
    isValid[Count[LowerCase, Greater[_2]]](s) ?= (s.count(_.isLower) > 2)
  }

  property("Empty.isValid") = forAll { (l: List[Int]) =>
    isValid[Empty](l) ?= l.isEmpty
  }

  property("Empty.showExpr") = secure {
    showExpr[Empty](List(1, 2)) ?= "isEmpty(List(1, 2))"
  }

  property("Empty.String.isValid") = forAll { (s: String) =>
    isValid[Empty](s) ?= s.isEmpty
  }

  property("Empty.String.showExpr") = secure {
    showExpr[Empty]("test") ?= "isEmpty(test)"
  }

  property("Exists.isValid") = forAll { (l: List[Int]) =>
    isValid[Exists[Less[_1]]](l) ?= l.exists(_ < 1)
  }

  property("Exists.showExpr") = secure {
    showExpr[Exists[Less[_1]]](List(1, 2, 3)) ?= "!(!(1 < 1) && !(2 < 1) && !(3 < 1))"
  }

  property("Forall.String.isValid") = forAll { (s: String) =>
    isValid[Forall[LowerCase]](s) ?= s.forall(_.isLower)
  }

  property("Forall.String.showExpr") = secure {
    showExpr[Forall[LowerCase]]("abc") ?= "(isLower('a') && isLower('b') && isLower('c'))"
  }

  property("Forall.String.showResult") = secure {
    showResult[Forall[LowerCase]]("ab") ?= "Predicate passed: (isLower('a') && isLower('b'))."
  }

  property("Head.isValid") = forAll { (l: List[Char]) =>
    isValid[Head[Digit]](l) ?= l.headOption.fold(false)(_.isDigit)
  }

  property("Head.showExpr.empty") = secure {
    showExpr[Head[Digit]](List.empty[Char]) ?= "<no element>"
  }

  property("Head.showExpr.nonEmpty") = secure {
    showExpr[Head[Digit]](List('a', 'b')) ?= "isDigit('a')"
  }

  property("Head.showResult") = secure {
    showResult[Head[Digit]](List('a', '1')) ?=
      "Predicate taking head(List(a, 1)) = a failed: Predicate failed: isDigit('a')."
  }

  property("Head.String.isValid") = forAll { (s: String) =>
    isValid[Head[Digit]](s) ?= s.headOption.fold(false)(_.isDigit)
  }

  property("Head.String.showExpr") = secure {
    showExpr[Head[Digit]]("ab") ?= "isDigit('a')"
  }

  property("Index.isValid") = forAll { (l: List[Char]) =>
    isValid[Index[2, Digit]](l) ?= l.lift(2).fold(false)(_.isDigit)
  }

  property("Index.showExpr") = secure {
    showExpr[Index[1, Digit]](List('a', 'b')) ?= "isDigit('b')"
  }

  property("Index.showResult.empty") = secure {
    showResult[Index[2, Digit]](List.empty[Char]) ?= "Predicate failed: empty collection."
  }

  property("Index.showResult.nonEmpty") = secure {
    showResult[Index[2, Digit]](List('a', 'b', 'c')) ?=
      "Predicate taking index(List(a, b, c), 2) = c failed: Predicate failed: isDigit('c')."
  }

  property("Last.isValid") = forAll { (l: List[Int]) =>
    isValid[Last[Greater[_5]]](l) ?= l.lastOption.fold(false)(_ > 5)
  }

  property("Last.showExpr") = secure {
    showExpr[Last[Greater[_5]]](List(1, 2, 3)) ?= "(3 > 5)"
  }

  property("Last.showResult") = secure {
    showResult[Last[Greater[_5]]](List(1, 2, 3)) ?=
      "Predicate taking last(List(1, 2, 3)) = 3 failed: Predicate failed: (3 > 5)."
  }

  property("Last.String.isValid") = forAll { (s: String) =>
    isValid[Last[Digit]](s) ?= s.lastOption.fold(false)(_.isDigit)
  }

  property("Last.String.showExpr") = secure {
    showExpr[Last[Digit]]("abc0") ?= "isDigit('0')"
  }

  property("Init.String.isValid") = forAll { (s: String) =>
    isValid[Init[LowerCase]](s) ?= s.toList.dropRight(1).forall(_.isLower)
  }

  property("Init.String.showExpr") = secure {
    showExpr[Init[LowerCase]]("abcd") ?= "(isLower('a') && isLower('b') && isLower('c'))"
  }

  property("Init.String.showResult") = secure {
    showResult[Init[LowerCase]]("abc") ?= "Predicate passed: (isLower('a') && isLower('b'))."
  }

  property("Tail.String.isValid") = forAll { (s: String) =>
    isValid[Tail[LowerCase]](s) ?= s.toList.drop(1).forall(_.isLower)
  }

  property("Tail.String.showExpr") = secure {
    showExpr[Tail[LowerCase]]("abcd") ?= "(isLower('b') && isLower('c') && isLower('d'))"
  }

  property("Tail.String.showResult") = secure {
    showResult[Tail[LowerCase]]("abc") ?= "Predicate passed: (isLower('b') && isLower('c'))."
  }

  property("MinSize.String.isValid") = forAll { (s: String) =>
    isValid[MinSize[_5]](s) ?= (s.length >= 5)
  }

  property("NonEmpty.String.isValid") = forAll { (s: String) =>
    isValid[NonEmpty](s) ?= s.nonEmpty
  }

  property("NonEmpty.String.showExpr") = secure {
    showExpr[NonEmpty]("test") ?= "!isEmpty(test)"
  }

  property("Size.isValid") = forAll { (l: List[Int]) =>
    isValid[Size[Greater[_5]]](l) ?= (l.size > 5)
  }

  property("Size.showExpr") = secure {
    showExpr[Size[Greater[_5]]](List(1, 2, 3)) ?= "(3 > 5)"
  }

  property("Size.String.isValid") = forAll { (s: String) =>
    isValid[Size[LessEqual[_10]]](s) ?= (s.length <= 10)
  }

  property("Size.String.showExpr") = secure {
    showExpr[Size[Greater[_5] And LessEqual[_10]]]("test") ?= "((4 > 5) && !(4 > 10))"
  }
}
