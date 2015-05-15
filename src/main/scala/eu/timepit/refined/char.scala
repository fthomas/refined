package eu.timepit.refined

object char {
  trait LowerCase

  trait UpperCase

  implicit val lowerCaseCharPredicate: Predicate[LowerCase, Char] =
    Predicate.instance(_.isLower, t => s"isLower('$t')")

  implicit val upperCaseCharPredicate: Predicate[UpperCase, Char] =
    Predicate.instance(_.isUpper, t => s"isUpper('$t')")
}
