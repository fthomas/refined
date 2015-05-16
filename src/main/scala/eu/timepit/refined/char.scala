package eu.timepit.refined

object char {
  /** Predicate that checks if a `Char` is a digit. */
  trait Digit

  /** Predicate that checks if a `Char` is a lower case character. */
  trait LowerCase

  /** Predicate that checks if a `Char` is an upper case character. */
  trait UpperCase

  implicit val digitPredicate: Predicate[Digit, Char] =
    Predicate.instance(_.isDigit, t => s"isDigit('$t')")

  implicit val lowerCasePredicate: Predicate[LowerCase, Char] =
    Predicate.instance(_.isLower, t => s"isLower('$t')")

  implicit val upperCasePredicate: Predicate[UpperCase, Char] =
    Predicate.instance(_.isUpper, t => s"isUpper('$t')")
}
