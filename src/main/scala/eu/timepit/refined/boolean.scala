package eu.timepit.refined

import shapeless.{ ::, HList, HNil }

object boolean {
  /** Constant predicate that is always true. */
  trait True

  /** Constant predicate that is always false. */
  trait False

  /** Negation of the predicate `P`. */
  trait Not[P]

  /** Conjunction of the predicates `A` and `B`. */
  trait And[A, B]

  /** Disjunction of the predicates `A` and `B`. */
  trait Or[A, B]

  /** Conjunction of all predicates in `PS`. */
  trait AllOf[PS]

  /** Disjunction of all predicates in `PS`. */
  trait AnyOf[PS]

  implicit def truePredicate[T]: Predicate[True, T] = alwaysTrue

  implicit def falsePredicate[T]: Predicate[False, T] = alwaysFalse

  implicit def notPredicate[P, T](implicit p: Predicate[P, T]): Predicate[Not[P], T] =
    new Predicate[Not[P], T] {
      def isValid(t: T): Boolean = !p.isValid(t)
      def show(t: T): String = s"!${p.show(t)}"

      override def validated(t: T): Option[String] =
        p.validated(t) match {
          case Some(_) => None
          case None => Some(s"Predicate ${p.show(t)} did not fail.")
        }
    }

  implicit def andPredicate[A, B, T](implicit pa: Predicate[A, T], pb: Predicate[B, T]): Predicate[A And B, T] =
    new Predicate[A And B, T] {
      def isValid(t: T): Boolean = pa.isValid(t) && pb.isValid(t)
      def show(t: T): String = s"(${pa.show(t)} && ${pb.show(t)})"

      override def validated(t: T): Option[String] =
        (pa.validated(t), pb.validated(t)) match {
          case (Some(sl), Some(sr)) =>
            Some(s"Both predicates of ${show(t)} failed. Left: $sl Right: $sr")
          case (Some(sl), None) =>
            Some(s"Left predicate of ${show(t)} failed: $sl")
          case (None, Some(sr)) =>
            Some(s"Right predicate of ${show(t)} failed: $sr")
          case _ => None
        }
    }

  implicit def orPredicate[A, B, T](implicit pa: Predicate[A, T], pb: Predicate[B, T]): Predicate[A Or B, T] =
    new Predicate[A Or B, T] {
      def isValid(t: T): Boolean = pa.isValid(t) || pb.isValid(t)
      def show(t: T): String = s"(${pa.show(t)} || ${pb.show(t)})"

      override def validated(t: T): Option[String] =
        (pa.validated(t), pb.validated(t)) match {
          case (Some(sl), Some(sr)) =>
            Some(s"Both predicates of ${show(t)} failed. Left: $sl Right: $sr")
          case _ => None
        }
    }

  implicit def allOfHNilPredicate[T]: Predicate[AllOf[HNil], T] = alwaysTrue

  implicit def allOfHConsPredicate[PH, PT <: HList, T](implicit ph: Predicate[PH, T], pt: Predicate[AllOf[PT], T]): Predicate[AllOf[PH :: PT], T] =
    Predicate.instance(t => ph.isValid(t) && pt.isValid(t), t => s"(${ph.show(t)} && ${pt.show(t)})")

  implicit def anyOfHNilPredicate[T]: Predicate[AnyOf[HNil], T] = alwaysFalse

  implicit def anyOfHConsPredicate[PH, PT <: HList, T](implicit ph: Predicate[PH, T], pt: Predicate[AnyOf[PT], T]): Predicate[AnyOf[PH :: PT], T] =
    Predicate.instance(t => ph.isValid(t) || pt.isValid(t), t => s"(${ph.show(t)} || ${pt.show(t)})")

  private def alwaysTrue[P, T]: Predicate[P, T] =
    Predicate.instance(_ => true, _ => "true")

  private def alwaysFalse[P, T]: Predicate[P, T] =
    Predicate.instance(_ => false, _ => "false")
}
