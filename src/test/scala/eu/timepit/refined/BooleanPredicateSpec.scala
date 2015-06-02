package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.boolean._
import eu.timepit.refined.char.{ Digit, Letter, UpperCase, Whitespace }
import eu.timepit.refined.numeric.{ Greater, Less }
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.{ ::, HNil }

class BooleanPredicateSpec extends Properties("BooleanPredicate") {

  type FF[Op[_, _]] = False Op False
  type FT[Op[_, _]] = False Op True
  type TF[Op[_, _]] = True Op False
  type TT[Op[_, _]] = True Op True

  property("True.isValid") = secure {
    Predicate[True, Unit].isValid(())
  }

  property("True.show") = secure {
    Predicate[True, Unit].show(()) ?= "true"
  }

  property("Not[True].isValid") = secure {
    Predicate[Not[True], Unit].notValid(())
  }

  property("Not[True].show") = secure {
    Predicate[Not[True], Unit].show(()) ?= "!true"
  }

  property("Not.consistent") = secure {
    consistent(Predicate[Not[True], Unit])(()) &&
      consistent(Predicate[Not[False], Unit])(())
  }

  property("False.isValid") = secure {
    Predicate[False, Unit].notValid(())
  }

  property("False.show") = secure {
    Predicate[False, Unit].show(()) ?= "false"
  }

  property("Not[False].isValid") = secure {
    Predicate[Not[False], Unit].isValid(())
  }

  property("And.isValid") = secure {
    Predicate[FF[And], Unit].notValid(()) &&
      Predicate[FT[And], Unit].notValid(()) &&
      Predicate[TF[And], Unit].notValid(()) &&
      Predicate[TT[And], Unit].isValid(())
  }

  property("And.consistent") = secure {
    consistent(Predicate[FF[And], Unit])(()) &&
      consistent(Predicate[FT[And], Unit])(()) &&
      consistent(Predicate[TF[And], Unit])(()) &&
      consistent(Predicate[TT[And], Unit])(())
  }

  property("And.show") = secure {
    Predicate[TF[And], Unit].show(()) ?= "(true && false)"
  }

  property("Or.isValid") = secure {
    Predicate[FF[Or], Unit].notValid(()) &&
      Predicate[FT[Or], Unit].isValid(()) &&
      Predicate[TF[Or], Unit].isValid(()) &&
      Predicate[TT[Or], Unit].isValid(())
  }

  property("Or.consistent") = secure {
    consistent(Predicate[FF[Or], Unit])(()) &&
      consistent(Predicate[FT[Or], Unit])(()) &&
      consistent(Predicate[TF[Or], Unit])(()) &&
      consistent(Predicate[TT[Or], Unit])(())
  }

  property("Or.show") = secure {
    Predicate[TF[Or], Unit].show(()) ?= "(true || false)"
  }

  property("Xor.isValid") = secure {
    Predicate[FF[Xor], Unit].notValid(()) &&
      Predicate[FT[Xor], Unit].isValid(()) &&
      Predicate[TF[Xor], Unit].isValid(()) &&
      Predicate[TT[Xor], Unit].notValid(())
  }

  property("Xor.consistent") = secure {
    consistent(Predicate[FF[Xor], Unit])(()) &&
      consistent(Predicate[FT[Xor], Unit])(()) &&
      consistent(Predicate[TF[Xor], Unit])(()) &&
      consistent(Predicate[TT[Xor], Unit])(())
  }

  property("Xor.show") = secure {
    Predicate[TF[Xor], Unit].show(()) ?= "(true ^ false)"
  }

  property("AllOf[Greater[_0] :: Less[_10] :: HNil].isValid") = forAll { (i: Int) =>
    Predicate[AllOf[Greater[_0] :: Less[_10] :: HNil], Int].isValid(i) ?=
      (i > 0 && i < 10)
  }

  property("AllOf[Greater[_0] :: Less[_10] :: HNil].show") = secure {
    Predicate[AllOf[Greater[_0] :: Less[_10] :: HNil], Int].show(5) ?=
      "((5 > 0) && ((5 < 10) && true))"
  }

  property("AnyOf[Digit :: Letter :: Whitespace :: HNil].isValid") = forAll { (c: Char) =>
    Predicate[AnyOf[Digit :: Letter :: Whitespace :: HNil], Char].isValid(c) ?=
      (c.isDigit || c.isLetter || c.isWhitespace)
  }

  property("AnyOf[Digit :: Letter :: Whitespace :: HNil].show") = secure {
    Predicate[AnyOf[Digit :: Letter :: Whitespace :: HNil], Char].show('c') ?=
      "(isDigit('c') || (isLetter('c') || (isWhitespace('c') || false)))"
  }

  property("OneOf[Digit :: Letter :: UpperCase :: HNil].isValid") = forAll { (c: Char) =>
    Predicate[OneOf[Digit :: Letter :: UpperCase :: HNil], Char].isValid(c) ?=
      List(c.isDigit, c.isLetter, c.isUpper).count(identity) == 1
  }

  property("OneOf[Digit :: Letter :: UpperCase :: HNil].show") = secure {
    Predicate[OneOf[Digit :: Letter :: UpperCase :: HNil], Char].show('c') ?=
      "oneOf(isDigit('c'), isLetter('c'), isUpper('c'), false)"
  }

  property("OneOf[_].contramap(identity).accumulateIsValid") = forAll { (c: Char) =>
    val p = Predicate[OneOf[Digit :: Letter :: UpperCase :: HNil], Char]
    p.contramap(identity[Char]).accumulateIsValid(c) ?= p.accumulateIsValid(c)
  }

  property("OneOf[_].contramap(identity).accumulateShow") = secure {
    val p = Predicate[OneOf[Digit :: Letter :: UpperCase :: HNil], Char]
    p.contramap(identity[Char]).accumulateShow('c') ?= p.accumulateShow('c')
  }
}
