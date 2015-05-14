package eu.timepit.refined

import eu.timepit.refined.boolean.Not

object collection {
  sealed trait Empty

  type NonEmpty = Not[Empty]

  sealed trait Forall[P]

  type Exists[P] = Not[Forall[Not[P]]]

  sealed trait Size[P]

  implicit def empty[T](implicit ev: T => TraversableOnce[_]): Predicate[Empty, T] =
    new Predicate[Empty, T] {
      def isValid(t: T): Boolean = t.isEmpty
      def show(t: T): String = s"isEmpty($t)"
    }

  implicit def forallPredicate[P, A, T[A] <: TraversableOnce[A]](implicit p: Predicate[P, A]): Predicate[Forall[P], T[A]] =
    new Predicate[Forall[P], T[A]] {
      def isValid(t: T[A]): Boolean = t.forall(p.isValid)
      def show(t: T[A]): String = t.toSeq.map(p.show).mkString("(", " && ", ")")
    }

  implicit def forallPredicateView[P, A, T](implicit p: Predicate[P, A], ev: T => TraversableOnce[A]): Predicate[Forall[P], T] =
    new Predicate[Forall[P], T] {
      def isValid(t: T): Boolean = t.forall(p.isValid)
      def show(t: T): String = t.toSeq.map(p.show).mkString("(", " && ", ")")
    }

  implicit def sizePredicate[P, T](implicit p: Predicate[P, Int], ev: T => TraversableOnce[_]): Predicate[Size[P], T] =
    new Predicate[Size[P], T] {
      override def isValid(t: T): Boolean = p.isValid(t.size)
      override def show(t: T): String = p.show(t.size)

      override def validated(t: T): Option[String] = {
        val s = t.size
        p.validated(s).map(msg => s"Predicate taking size($t) = $s failed: $msg")
      }
    }
}
