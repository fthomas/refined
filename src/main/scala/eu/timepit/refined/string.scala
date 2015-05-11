package eu.timepit.refined

import eu.timepit.refined.generic.{ Empty, Length }

object string {
  sealed trait LowerCase

  sealed trait UpperCase

  implicit val emptyStringPredicate: Predicate[Empty, String] =
    new Predicate[Empty, String] {
      def isValid(t: String): Boolean = t.isEmpty
      def show(t: String): String = s"isEmpty($t)"
    }

  implicit def lengthStringPredicate[P](implicit p: Predicate[P, Int]): Predicate[Length[P], String] =
    new Predicate[Length[P], String] {
      def isValid(t: String): Boolean = p.isValid(t.length)
      def show(t: String): String = s"${p.show(t.length)}"

      override def validated(t: String): Option[String] =
        p.validated(t.length).map(s => s"Length of '$t': $s")
    }

  implicit val lowerCaseStringPredicate: Predicate[LowerCase, String] =
    new Predicate[LowerCase, String] {
      def isValid(t: String): Boolean = t.forall(_.isLower)
      def show(t: String): String = s"isLowerCase($t)"
    }

  implicit val upperCaseStringPredicate: Predicate[UpperCase, String] =
    new Predicate[UpperCase, String] {
      def isValid(t: String): Boolean = t.forall(_.isUpper)
      def show(t: String): String = s"isUpperCase($t)"
    }
}
