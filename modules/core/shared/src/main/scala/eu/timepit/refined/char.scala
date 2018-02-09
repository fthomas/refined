package eu.timepit.refined

import eu.timepit.refined.api.Validate
import eu.timepit.refined.boolean.Or

/** Module for `Char` related predicates. */
object char {

  /** Predicate that checks if a `Char` is a digit. */
  final case class Digit()

  /** Predicate that checks if a `Char` is a letter. */
  final case class Letter()

  /** Predicate that checks if a `Char` is a lower case character. */
  final case class LowerCase()

  /** Predicate that checks if a `Char` is an upper case character. */
  final case class UpperCase()

  /** Predicate that checks if a `Char` is white space. */
  final case class Whitespace()

  /** Predicate that checks if a `Char` is a letter or digit. */
  type LetterOrDigit = Letter Or Digit

  object Digit {
    implicit def digitValidate: Validate.Plain[Char, Digit] =
      Validate.fromPredicate(_.isDigit, t => s"isDigit('$t')", Digit())
  }

  object Letter {
    implicit def letterValidate: Validate.Plain[Char, Letter] =
      Validate.fromPredicate(_.isLetter, t => s"isLetter('$t')", Letter())
  }

  object LowerCase {
    implicit def lowerCaseValidate: Validate.Plain[Char, LowerCase] =
      Validate.fromPredicate(_.isLower, t => s"isLower('$t')", LowerCase())
  }

  object UpperCase {
    implicit def upperCaseValidate: Validate.Plain[Char, UpperCase] =
      Validate.fromPredicate(_.isUpper, t => s"isUpper('$t')", UpperCase())
  }

  object Whitespace {
    implicit def whitespaceValidate: Validate.Plain[Char, Whitespace] =
      Validate.fromPredicate(_.isWhitespace, t => s"isWhitespace('$t')", Whitespace())
  }
}
