package refined

object boolean {
  final case class Not[P](p: P)

  implicit def notPredicate[P, X](implicit pt: Predicate[P, X]): Predicate[Not[P], X] =
    new Predicate[Not[P], X] {
      def validate(p: Not[P], x: X): Option[String] =
        pt.validate(p.p, x).fold(Option("A sub-predicate didn't fail"))(_ => Option.empty)
    }

  final case class And[A, B](a: A, b: B)

  implicit def andPredicate[A, B, X](implicit pa: Predicate[A, X], pb: Predicate[B, X]): Predicate[And[A, B], X] =
    new Predicate[And[A, B], X] {
      def validate(p: And[A, B], x: X): Option[String] =
        pa.validate(p.a, x).orElse(pb.validate(p.b, x))
    }

  final case class Or[A, B](a: A, b: B)

  implicit def orPredicate[A, B, X](implicit pa: Predicate[A, X], pb: Predicate[B, X]): Predicate[Or[A, B], X] =
    new Predicate[Or[A, B], X] {
      def validate(p: Or[A, B], x: X): Option[String] =
        if (pa.validate(p.a, x).isEmpty) None else pb.validate(p.b, x)
    }
}
