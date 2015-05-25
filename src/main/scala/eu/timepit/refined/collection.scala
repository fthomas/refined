package eu.timepit.refined

import eu.timepit.refined.boolean.Not
import eu.timepit.refined.numeric.{ GreaterEqual, LessEqual }

object collection {
  /**
   * Predicate that counts the number of elements in a `TraversableOnce`
   * which satisfy the predicate `PA` and passes the result to the numeric
   * predicate `PC`.
   */
  trait Count[PA, PC]

  /** Predicate that checks if a `TraversableOnce` is empty. */
  trait Empty

  /**
   * Predicate that checks if the predicate `P` holds for all elements of a
   * `TraversableOnce`.
   */
  trait Forall[P]

  /**
   * Predicate that checks if the size of a `TraversableOnce` satisfies the
   * predicate `P`.
   */
  trait Size[P]

  /** Predicate that checks if a `TraversableOnce` is not empty. */
  type NonEmpty = Not[Empty]

  /**
   * Predicate that checks if the predicate `P` holds for some elements of a
   * `TraversableOnce`.
   */
  type Exists[P] = Not[Forall[Not[P]]]

  /**
   * Predicate that checks if the size of a `TraversableOnce` is greater than
   * or equal to `N`.
   */
  type MinSize[N] = Size[GreaterEqual[N]]

  /**
   * Predicate that checks if the size of a `TraversableOnce` is less than
   * or equal to `N`.
   */
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
    Predicate.instance(_.forall(p.isValid), _.toSeq.map(p.show).mkString("(", " && ", ")"))

  implicit def forallPredicateView[P, A, T](implicit p: Predicate[P, A], ev: T => TraversableOnce[A]): Predicate[Forall[P], T] =
    forallPredicate.contramap(ev)

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
