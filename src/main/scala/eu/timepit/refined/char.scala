package eu.timepit.refined

object char {
  trait Digit

  trait LowerCase

  trait UpperCase

  implicit val digitPredicate: Predicate[Digit, Char] =
    Predicate.instance(_.isDigit, t => s"isDigit('$t')")

  implicit val lowerCasePredicate: Predicate[LowerCase, Char] =
    Predicate.instance(_.isLower, t => s"isLower('$t')")

  implicit val upperCasePredicate: Predicate[UpperCase, Char] =
    Predicate.instance(_.isUpper, t => s"isUpper('$t')")
}
