package eu.timepit.refined

import eu.timepit.refined.generic.{ Length, NonEmpty }

object string {
  implicit val nonEmptyStringPredicate: Predicate[NonEmpty, String] =
    new Predicate[NonEmpty, String] {
      def validate(x: String): Option[String] =
        if (x.nonEmpty) None
        else Some("String is empty")
    }

  implicit def lengthStringPredicate[P](implicit p: Predicate[P, Int]): Predicate[Length[P], String] =
    new Predicate[Length[P], String] {
      def validate(x: String): Option[String] =
        p.validate(x.length).fold(Option.empty[String])(s => Some(s"Length predicate failed: $s"))
    }

  sealed trait LowerCase

  implicit val lowerCaseStringPredicate: Predicate[LowerCase, String] =
    new Predicate[LowerCase, String] {
      def validate(x: String): Option[String] =
        if (x.forall(_.isLower)) None
        else Some("Not all chars are lower-case")
    }

  sealed trait UpperCase

  implicit val upperCaseStringPredicate: Predicate[UpperCase, String] =
    new Predicate[UpperCase, String] {
      def validate(x: String): Option[String] =
        if (x.forall(_.isUpper)) None
        else Some("Not all chars are upper-case")
    }
}
