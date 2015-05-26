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

  /** Exclusive disjunction of the predicates `A` and `B`. */
  trait Xor[A, B]

  /** Conjunction of all predicates in `PS`. */
  trait AllOf[PS]

  /** Disjunction of all predicates in `PS`. */
  trait AnyOf[PS]

  /** Exclusive disjunction of all predicates in `PS`. */
  trait OneOf[PS]

  implicit def truePredicate[T]: Predicate[True, T] =
    Predicate.alwaysTrue

  implicit def falsePredicate[T]: Predicate[False, T] =
    Predicate.alwaysFalse

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

  implicit def xorPredicate[A, B, T](implicit pa: Predicate[A, T], pb: Predicate[B, T]): Predicate[A Xor B, T] =
    new Predicate[A Xor B, T] {
      def isValid(t: T): Boolean = pa.isValid(t) ^ pb.isValid(t)
      def show(t: T): String = s"(${pa.show(t)} ^ ${pb.show(t)})"

      override def validated(t: T): Option[String] =
        (pa.validated(t), pb.validated(t)) match {
          case (Some(sl), Some(sr)) =>
            Some(s"Both predicates of ${show(t)} failed. Left: $sl Right: $sr")
          case (None, None) =>
            Some(s"Both predicates of ${show(t)} succeeded.")
          case _ => None
        }
    }

  implicit def allOfHNilPredicate[T]: Predicate[AllOf[HNil], T] =
    Predicate.alwaysTrue

  implicit def allOfHConsPredicate[PH, PT <: HList, T](implicit ph: Predicate[PH, T], pt: Predicate[AllOf[PT], T]): Predicate[AllOf[PH :: PT], T] =
    Predicate.instance(
      t => ph.isValid(t) && pt.isValid(t),
      t => s"(${ph.show(t)} && ${pt.show(t)})")

  implicit def anyOfHNilPredicate[T]: Predicate[AnyOf[HNil], T] =
    Predicate.alwaysFalse

  implicit def anyOfHConsPredicate[PH, PT <: HList, T](implicit ph: Predicate[PH, T], pt: Predicate[AnyOf[PT], T]): Predicate[AnyOf[PH :: PT], T] =
    Predicate.instance(
      t => ph.isValid(t) || pt.isValid(t),
      t => s"(${ph.show(t)} || ${pt.show(t)})")

  implicit def oneOfHNilPredicate[T]: Predicate[OneOf[HNil], T] =
    Predicate.alwaysFalse

  implicit def oneOfHConsPredicate[PH, PT <: HList, T](implicit ph: Predicate[PH, T], pt: Predicate[OneOf[PT], T]): Predicate[OneOf[PH :: PT], T] =
    new Predicate[OneOf[PH :: PT], T] {
      def isValid(t: T): Boolean = accumulateIsValid(t).count(identity) == 1
      def show(t: T): String = accumulateShow(t).mkString("oneOf(", ", ", ")")

      override def accumulateIsValid(t: T): List[Boolean] =
        ph.isValid(t) :: pt.accumulateIsValid(t)

      override def accumulateShow(t: T): List[String] =
        ph.show(t) :: pt.accumulateShow(t)
    }
}
