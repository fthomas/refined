package eu.timepit.refined

import eu.timepit.refined.boolean.Not
import eu.timepit.refined.numeric.{ GreaterEqual, LessEqual }

object collection {
  trait Count[PA, PC]

  trait Empty

  trait Forall[P]

  trait Size[P]

  type NonEmpty = Not[Empty]

  type Exists[P] = Not[Forall[Not[P]]]

  type MinSize[N] = Size[GreaterEqual[N]]

  type MaxSize[N] = Size[LessEqual[N]]

  implicit def countPredicate[PA, PC, A, T](implicit pa: Predicate[PA, A], pc: Predicate[PC, Int], ev: T => TraversableOnce[A]): Predicate[Count[PA, PC], T] =
    new Predicate[Count[PA, PC], T] {
      def isValid(t: T): Boolean = pc.isValid(count(t))
      def show(t: T): String = pc.show(count(t))

      override def validated(t: T): Option[String] = {
        val c = count(t)
        val expr = t.toSeq.map(pa.show).mkString("count(", ", ", ")")
        pc.validated(c).map(msg => s"Predicate taking $expr = $c failed: $msg")
      }

      private def count(t: T): Int = t.count(pa.isValid)
    }

  implicit def emptyPredicate[T](implicit ev: T => TraversableOnce[_]): Predicate[Empty, T] =
    Predicate.instance(_.isEmpty, t => s"isEmpty($t)")

  implicit def forallPredicate[P, A, T[A] <: TraversableOnce[A]](implicit p: Predicate[P, A]): Predicate[Forall[P], T[A]] =
    Predicate.instance(_.forall(p.isValid), forallShow(p.show))

  implicit def forallPredicateView[P, A, T](implicit p: Predicate[P, A], ev: T => TraversableOnce[A]): Predicate[Forall[P], T] =
    Predicate.instance(_.forall(p.isValid), ev andThen forallShow(p.show))

  private def forallShow[A](f: A => String): TraversableOnce[A] => String =
    _.toSeq.map(f).mkString("(", " && ", ")")

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
