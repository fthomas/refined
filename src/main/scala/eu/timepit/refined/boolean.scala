package eu.timepit.refined

object boolean {
  sealed trait True

  sealed trait False

  sealed trait Not[P]

  sealed trait And[A, B]

  sealed trait Or[A, B]

  implicit def truePredicate[T]: Predicate[True, T] =
    new Predicate[True, T] {
      def isValid(t: T): Boolean = true
      def show(t: T): String = "true"
    }

  implicit def falsePredicate[T]: Predicate[False, T] =
    new Predicate[False, T] {
      def isValid(t: T): Boolean = false
      def show(t: T): String = "false"
    }

  implicit def notPredicate[P, T](implicit p: Predicate[P, T]): Predicate[Not[P], T] =
    new Predicate[Not[P], T] {
      def isValid(t: T): Boolean = !p.isValid(t)
      def show(t: T): String = s"!${p.show(t)}"
    }

  implicit def andPredicate[A, B, T](implicit pa: Predicate[A, T], pb: Predicate[B, T]): Predicate[A And B, T] =
    new Predicate[A And B, T] {
      def isValid(t: T): Boolean = pa.isValid(t) && pb.isValid(t)
      def show(t: T): String = s"(${pa.show(t)} && ${pb.show(t)})"
    }

  implicit def orPredicate[A, B, T](implicit pa: Predicate[A, T], pb: Predicate[B, T]): Predicate[A Or B, T] =
    new Predicate[A Or B, T] {
      def isValid(t: T): Boolean = pa.isValid(t) || pb.isValid(t)
      def show(t: T): String = s"(${pa.show(t)} || ${pb.show(t)})"
    }
}
