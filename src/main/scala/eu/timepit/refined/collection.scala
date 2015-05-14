package eu.timepit.refined

import eu.timepit.refined.boolean.Not

object collection {
  sealed trait Empty

  type NonEmpty = Not[Empty]

  sealed trait Forall[P]

  type Exists[P] = Not[Forall[Not[P]]]

  sealed trait Length[P]

  implicit def empty(): Predicate[Empty, C] =
    new Predicate[Empty, C] {
      def isValid()
    }

  implicit def forallPredicate[P, C, A](implicit p: Predicate[P, A], ev: C => TraversableOnce[A]): Predicate[Forall[P], C] =
    new Predicate[Forall[P], C] {
      def isValid(t: C): Boolean = t.forall(p.isValid)
      def show(t: C): String = t.toSeq.map(p.show).mkString("(", " && ", ")")
    }
}
