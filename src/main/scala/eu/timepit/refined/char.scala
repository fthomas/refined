package eu.timepit.refined

import eu.timepit.refined.generic._

object char {
  implicit val lowerCaseCharPredicate: Predicate[LowerCase, Char] =
    new Predicate[LowerCase, Char] {
      def isValid(t: Char): Boolean = t.isLower
      def show(t: Char): String = s"isLower('$t')"
    }

  implicit val upperCaseCharPredicate: Predicate[UpperCase, Char] =
    new Predicate[UpperCase, Char] {
      def isValid(t: Char): Boolean = t.isUpper
      def show(t: Char): String = s"isUpper('$t')"
    }
}
