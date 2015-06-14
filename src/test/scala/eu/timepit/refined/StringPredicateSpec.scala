package eu.timepit.refined

import eu.timepit.refined.boolean._
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class StringPredicateSpec extends Properties("StringPredicate") {

  property("Empty.isValid") = forAll { (s: String) =>
    Predicate[Empty, String].isValid(s) ?= s.isEmpty
  }

  property("Empty.show") = secure {
    Predicate[Empty, String].show("test") ?= "isEmpty(test)"
  }

  property("NonEmpty.isValid") = forAll { (s: String) =>
    Predicate[NonEmpty, String].isValid(s) ?= s.nonEmpty
  }

  property("NonEmpty.show") = secure {
    Predicate[NonEmpty, String].show("test") ?= "!isEmpty(test)"
  }

  property("Forall[LowerCase].isValid") = forAll { (s: String) =>
    Predicate[Forall[LowerCase], String].isValid(s) ?= s.forall(_.isLower)
  }

  property("Forall[LowerCase].show") = secure {
    Predicate[Forall[LowerCase], String].show("abc") ?=
      "(isLower('a') && isLower('b') && isLower('c'))"
  }

  property("Forall[UpperCase].isValid") = forAll { (s: String) =>
    Predicate[Forall[UpperCase], String].isValid(s) ?= s.forall(_.isUpper)
  }

  property("Forall[UpperCase].show") = secure {
    Predicate[Forall[UpperCase], String].show("abc") ?=
      "(isUpper('a') && isUpper('b') && isUpper('c'))"
  }

  property("Head[Letter].isValid") = forAll { (s: String) =>
    Predicate[Head[Letter], String].isValid(s) ?= s.headOption.fold(false)(_.isLetter)
  }

  property("Last[Letter].show") = secure {
    Predicate[Last[Letter], String].show("abc0") ?= "isLetter('0')"
  }

  property("Size.isValid") = forAll { (s: String) =>
    Predicate[Size[LessEqual[_10]], String].isValid(s) ?= (s.length <= 10)
  }

  property("Size.validated") = forAll { (s: String) =>
    Predicate[Size[LessEqual[_10]], String].validated(s).isEmpty ?= (s.length <= 10)
  }

  property("Size.show") = secure {
    type P = Size[Greater[_5] And LessEqual[_10]]
    Predicate[P, String].show("test") ?= "((4 > 5) && !(4 > 10))"
  }

  property("Count[LowerCase, Greater[_2]].isValid") = forAll { (s: String) =>
    Predicate[Count[LowerCase, Greater[_2]], String].isValid(s) ?= (s.count(_.isLower) > 2)
  }

  property("MinSize[_5].isValid") = forAll { (s: String) =>
    Predicate[MinSize[_5], String].isValid(s) ?= (s.length >= 5)
  }

  property("MatchesRegex[R].isValid") = forAll { (s: String) =>
    Predicate[MatchesRegex[W.`".{2,10}"`.T], String].isValid(s) ?= s.matches(".{2,10}")
  }

  property("MatchesRegex[R].show") = secure {
    Predicate[MatchesRegex[W.`".{2,10}"`.T], String].show("Hello") ?=
      """"Hello".matches(".{2,10}")"""
  }

  property("EndsWith[S].isValid") = secure {
    val s = "abcd"
    Predicate[EndsWith[W.`"cd"`.T], String].isValid(s) ?= s.endsWith("cd")
  }

  property("EndsWith[S].show") = secure {
    val s = "abcd"
    Predicate[EndsWith[W.`"cd"`.T], String].show(s) ?= """"abcd".endsWith("cd")"""
  }

  property("StartsWith[S].isValid") = secure {
    val s = "abcd"
    Predicate[StartsWith[W.`"ab"`.T], String].isValid(s) ?= s.startsWith("ab")
  }

  property("StartsWith[S].show") = secure {
    val s = "abcd"
    Predicate[StartsWith[W.`"ab"`.T], String].show(s) ?= """"abcd".startsWith("ab")"""
  }

  property("Regex.isValid") = secure {
    Predicate[Regex, String].isValid(".*")
  }

  property("Regex.notValid") = secure {
    Predicate[Regex, String].notValid("(a|b")
  }

  property("Regex.show") = secure {
    Predicate[Regex, String].show("(a|b)") ?= """isValidRegex("(a|b)")"""
  }

  property("Regex.validated success") = secure {
    Predicate[Regex, String].validated("(a|b)") ?= None
  }

  property("Regex.validated failure") = secure {
    Predicate[Regex, String].validated("(a|b") ?=
      Some(
        """Predicate isValidRegex("(a|b") failed: Unclosed group near index 4
          |(a|b
          |    ^""".stripMargin)
  }
}
