package eu.timepit.refined

object boolean {
  sealed trait Not[P]

  implicit def notPredicate[P, X](implicit pt: Predicate[P, X]): Predicate[Not[P], X] =
    new Predicate[Not[P], X] {
      def validate(x: X): Option[String] =
        pt.validate(x).fold(Option(s"A sub-predicate didn't fail"))(_ => None)
    }

  sealed trait And[A, B]

  implicit def andPredicate[A, B, X](implicit pa: Predicate[A, X], pb: Predicate[B, X]): Predicate[And[A, B], X] =
    new Predicate[And[A, B], X] {
      def validate(x: X): Option[String] =
        pa.validate(x).orElse(pb.validate(x))
    }

  sealed trait Or[A, B]

  implicit def orPredicate[A, B, X](implicit pa: Predicate[A, X], pb: Predicate[B, X]): Predicate[Or[A, B], X] =
    new Predicate[Or[A, B], X] {
      def validate(x: X): Option[String] =
        if (pa.validate(x).isEmpty) None else pb.validate(x)
    }

  sealed trait True

  implicit def truePredicate[X]: Predicate[True, X] =
    new Predicate[True, X] {
      def validate(x: X): Option[String] = None
    }

  sealed trait False

  implicit def falsePredicate[X]: Predicate[False, X] =
    new Predicate[False, X] {
      def validate(x: X): Option[String] = Some("False always fails")
    }
}
