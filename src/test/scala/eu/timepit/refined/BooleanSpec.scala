package eu.timepit.refined

import eu.timepit.refined.boolean._
import eu.timepit.refined.char.{ Digit, Letter, Whitespace }
import eu.timepit.refined.numeric.{ Greater, Less }
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.{ ::, HNil }

class BooleanSpec extends Properties("boolean") {
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

  property("And.validated") = secure {
    Predicate[FF[And], Unit].validated(()).nonEmpty &&
      Predicate[FT[And], Unit].validated(()).nonEmpty &&
      Predicate[TF[And], Unit].validated(()).nonEmpty &&
      Predicate[TT[And], Unit].validated(()).isEmpty
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

  property("Or.validated") = secure {
    Predicate[FF[Or], Unit].validated(()).nonEmpty &&
      Predicate[FT[Or], Unit].validated(()).isEmpty &&
      Predicate[TF[Or], Unit].validated(()).isEmpty &&
      Predicate[TT[Or], Unit].validated(()).isEmpty
  }

  property("Or.show") = secure {
    Predicate[TF[Or], Unit].show(()) ?= "(true || false)"
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
}
