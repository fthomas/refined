package eu.timepit.refined

object boolean {
  sealed trait Not[P]

  sealed trait And[A, B]

  implicit def notPredicate[P, T](implicit pt: Predicate[P, T]): Predicate[Not[P], T] =
    new Predicate[Not[P], T] {
      def isValid(t: T): Boolean = !pt.isValid(t)
      def show(t: T): String = s"~(${pt.show(t)})"
    }

  implicit def andPredicate[A, B, X](implicit pa: Predicate[A, X], pb: Predicate[B, X]): Predicate[And[A, B], X] =
    new Predicate[And[A, B], X] {
      def validate(x: X): Option[String] =
        if (pa.isValid(x) && pb.isValid(x)) None else Some(msg(x))
      //if (pa.validate(x).isEmpty && pb.validate(x))
      //pa.validate(x).orElse(pb.validate(x))

      override def msg(x: X): String =
        s"(${pa.msg(x)} && ${pb.msg(x)})"
    }

  sealed trait Or[A, B]

  implicit def orPredicate[A, B, X](implicit pa: Predicate[A, X], pb: Predicate[B, X]): Predicate[Or[A, B], X] =
    new Predicate[Or[A, B], X] {
      def validate(x: X): Option[String] =
        if (pa.validate(x).isEmpty) None else pb.validate(x)
    }

  sealed trait True

  implicit def truePredicate[T]: Predicate[True, T] =
    new Predicate[True, T] {
      def validate(t: T): Option[String] = None
    }

  sealed trait False

  implicit def falsePredicate[T]: Predicate[False, T] =
    new Predicate[False, T] {
      def validate(t: T): Option[String] = Some("False always fails")
    }
}
