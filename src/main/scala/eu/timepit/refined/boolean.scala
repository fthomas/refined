package eu.timepit.refined

import eu.timepit.refined.InferenceRuleAlias.==>
import eu.timepit.refined.boolean._
import shapeless.{ ::, HList, HNil }

object boolean extends BooleanPredicates with BooleanInferenceRules0 {

  /** Constant predicate that is always `true`. */
  trait True

  /** Constant predicate that is always `false`. */
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
}

trait BooleanPredicates {

  implicit def truePredicate[T]: Predicate[True, T] =
    Predicate.alwaysValid

  implicit def falsePredicate[T]: Predicate[False, T] =
    Predicate.alwaysInvalid

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
    Predicate.alwaysValid

  implicit def allOfHConsPredicate[PH, PT <: HList, T](implicit ph: Predicate[PH, T], pt: Predicate[AllOf[PT], T]): Predicate[AllOf[PH :: PT], T] =
    Predicate.instance(
      t => ph.isValid(t) && pt.isValid(t),
      t => s"(${ph.show(t)} && ${pt.show(t)})")

  implicit def anyOfHNilPredicate[T]: Predicate[AnyOf[HNil], T] =
    Predicate.alwaysInvalid

  implicit def anyOfHConsPredicate[PH, PT <: HList, T](implicit ph: Predicate[PH, T], pt: Predicate[AnyOf[PT], T]): Predicate[AnyOf[PH :: PT], T] =
    Predicate.instance(
      t => ph.isValid(t) || pt.isValid(t),
      t => s"(${ph.show(t)} || ${pt.show(t)})")

  implicit def oneOfHNilPredicate[T]: Predicate[OneOf[HNil], T] =
    Predicate.alwaysInvalid

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

trait BooleanInferenceRules0 extends BooleanInferenceRules1 {

  implicit def minimalTautology[A]: A ==> A =
    InferenceRule.alwaysValid

  implicit def doubleNegationElimination[A, B](implicit p1: A ==> B): Not[Not[A]] ==> B =
    p1.adapted

  implicit def doubleNegationIntroduction[A, B](implicit p1: A ==> B): A ==> Not[Not[B]] =
    p1.adapted

  implicit def conjunctionAssociativity[A, B, C]: ((A And B) And C) ==> (A And (B And C)) =
    InferenceRule.alwaysValid

  implicit def conjunctionCommutativity[A, B]: (A And B) ==> (B And A) =
    InferenceRule.alwaysValid

  implicit def conjunctionEliminationR[A, B, C](implicit p1: B ==> C): (A And B) ==> C =
    p1.adapted

  implicit def disjunctionAssociativity[A, B, C]: ((A Or B) Or C) ==> (A Or (B Or C)) =
    InferenceRule.alwaysValid

  implicit def disjunctionCommutativity[A, B]: (A Or B) ==> (B Or A) =
    InferenceRule.alwaysValid

  implicit def disjunctionIntroductionL[A, B]: A ==> (A Or B) =
    InferenceRule.alwaysValid

  implicit def disjunctionIntroductionR[A, B]: B ==> (A Or B) =
    InferenceRule.alwaysValid

  implicit def deMorgansLaw1[A, B]: Not[A And B] ==> (Not[A] Or Not[B]) =
    InferenceRule.alwaysValid

  implicit def deMorgansLaw2[A, B]: Not[A Or B] ==> (Not[A] And Not[B]) =
    InferenceRule.alwaysValid

  implicit def xorCommutativity[A, B]: (A Xor B) ==> (B Xor A) =
    InferenceRule.alwaysValid

  implicit def modusTollens[A, B](implicit p1: A ==> B): Not[B] ==> Not[A] =
    p1.adapted
}

trait BooleanInferenceRules1 {

  implicit def conjunctionEliminationL[A, B, C](implicit p1: A ==> C): (A And B) ==> C =
    p1.adapted

  implicit def hypotheticalSyllogism[A, B, C](implicit p1: A ==> B, p2: B ==> C): A ==> C =
    p1 && p2
}
