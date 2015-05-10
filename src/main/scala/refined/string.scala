package refined

object string {
  final case class NonEmpty()

  implicit val nonEmptyStringPredicate: Predicate[NonEmpty, String] =
    new Predicate[NonEmpty, String] {
      def validate(p: NonEmpty, x: String): Option[String] =
        if (x.nonEmpty) None
        else Some("String is empty")
    }

  final case class LowerCase()

  implicit val lowerCaseStringPredicate: Predicate[LowerCase, String] =
    new Predicate[LowerCase, String] {
      def validate(p: LowerCase, x: String): Option[String] =
        if (x.forall(_.isLower)) None
        else Some("Not all chars are lower-case")
    }

  final case class UpperCase()

  implicit val upperCaseStringPredicate: Predicate[UpperCase, String] =
    new Predicate[UpperCase, String] {
      def validate(p: UpperCase, x: String): Option[String] =
        if (x.forall(_.isUpper)) None
        else Some("Not all chars are upper-case")
    }
}
