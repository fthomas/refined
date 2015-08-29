package eu.timepit.refined

import eu.timepit.refined.InferenceRule.==>
import eu.timepit.refined.boolean._
import shapeless.{::, HList, HNil}

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

private[refined] trait BooleanPredicates {

  implicit def truePredicate[T]: Predicate[True, T] =
    Predicate.alwaysValid

  implicit def falsePredicate[T]: Predicate[False, T] =
    Predicate.alwaysInvalid

  implicit def notPredicate[P, T](implicit p: Predicate[P, T]): Predicate[Not[P], T] =
    new Predicate[Not[P], T] {
      def isValid(t: T): Boolean = !p.isValid(t)
      def show(t: T): String = s"!${p.show(t)}"

      override def validate(t: T): Option[String] =
        p.validate(t) match {
          case Some(_) => None
          case None => Some(s"Predicate ${p.show(t)} did not fail.")
        }

      override val isConstant: Boolean = p.isConstant
    }

  implicit def andPredicate[A, B, T](implicit pa: Predicate[A, T], pb: Predicate[B, T]): Predicate[A And B, T] =
    new Predicate[A And B, T] {
      def isValid(t: T): Boolean = pa.isValid(t) && pb.isValid(t)
      def show(t: T): String = s"(${pa.show(t)} && ${pb.show(t)})"

      override def validate(t: T): Option[String] =
        (pa.validate(t), pb.validate(t)) match {
          case (Some(sl), Some(sr)) =>
            Some(s"Both predicates of ${show(t)} failed. Left: $sl Right: $sr")
          case (Some(sl), None) =>
            Some(s"Left predicate of ${show(t)} failed: $sl")
          case (None, Some(sr)) =>
            Some(s"Right predicate of ${show(t)} failed: $sr")
          case _ => None
        }

      override val isConstant: Boolean = pa.isConstant && pb.isConstant
    }

  implicit def orPredicate[A, B, T](implicit pa: Predicate[A, T], pb: Predicate[B, T]): Predicate[A Or B, T] =
    new Predicate[A Or B, T] {
      def isValid(t: T): Boolean = pa.isValid(t) || pb.isValid(t)
      def show(t: T): String = s"(${pa.show(t)} || ${pb.show(t)})"

      override def validate(t: T): Option[String] =
        (pa.validate(t), pb.validate(t)) match {
          case (Some(sl), Some(sr)) =>
            Some(s"Both predicates of ${show(t)} failed. Left: $sl Right: $sr")
          case _ => None
        }

      override val isConstant: Boolean = pa.isConstant && pb.isConstant
    }

  implicit def xorPredicate[A, B, T](implicit pa: Predicate[A, T], pb: Predicate[B, T]): Predicate[A Xor B, T] =
    new Predicate[A Xor B, T] {
      def isValid(t: T): Boolean = pa.isValid(t) ^ pb.isValid(t)
      def show(t: T): String = s"(${pa.show(t)} ^ ${pb.show(t)})"

      override def validate(t: T): Option[String] =
        (pa.validate(t), pb.validate(t)) match {
          case (Some(sl), Some(sr)) =>
            Some(s"Both predicates of ${show(t)} failed. Left: $sl Right: $sr")
          case (None, None) =>
            Some(s"Both predicates of ${show(t)} succeeded.")
          case _ => None
        }

      override val isConstant: Boolean = pa.isConstant && pb.isConstant
    }

  implicit def allOfHNilPredicate[T]: Predicate[AllOf[HNil], T] =
    Predicate.alwaysValid

  implicit def allOfHConsPredicate[PH, PT <: HList, T](implicit ph: Predicate[PH, T], pt: Predicate[AllOf[PT], T]): Predicate[AllOf[PH :: PT], T] =
    Predicate.instance(
      t => ph.isValid(t) && pt.isValid(t),
      t => s"(${ph.show(t)} && ${pt.show(t)})",
      ph.isConstant && pt.isConstant
    )

  implicit def anyOfHNilPredicate[T]: Predicate[AnyOf[HNil], T] =
    Predicate.alwaysInvalid

  implicit def anyOfHConsPredicate[PH, PT <: HList, T](implicit ph: Predicate[PH, T], pt: Predicate[AnyOf[PT], T]): Predicate[AnyOf[PH :: PT], T] =
    Predicate.instance(
      t => ph.isValid(t) || pt.isValid(t),
      t => s"(${ph.show(t)} || ${pt.show(t)})",
      ph.isConstant && pt.isConstant
    )

  implicit def oneOfHNilPredicate[T]: Predicate[OneOf[HNil], T] =
    Predicate.alwaysInvalid

  implicit def oneOfHConsPredicate[PH, PT <: HList, T](implicit ph: Predicate[PH, T], pt: Predicate[OneOf[PT], T]): Predicate[OneOf[PH :: PT], T] =
    new Predicate[OneOf[PH :: PT], T] {
      def isValid(t: T): Boolean = accumulateIsValid(t).count(identity) == 1
      def show(t: T): String = accumulateShow(t).mkString("oneOf(", ", ", ")")

      override val isConstant: Boolean = ph.isConstant && pt.isConstant

      override def accumulateIsValid(t: T): List[Boolean] =
        ph.isValid(t) :: pt.accumulateIsValid(t)

      override def accumulateShow(t: T): List[String] =
        ph.show(t) :: pt.accumulateShow(t)
    }
}

private[refined] trait BooleanInferenceRules0 extends BooleanInferenceRules1 {

  implicit def minimalTautology[A]: A ==> A =
    InferenceRule.alwaysValid("minimalTautology")

  implicit def doubleNegationElimination[A, B](implicit p1: A ==> B): Not[Not[A]] ==> B =
    p1.adapt("doubleNegationElimination(%s)")

  implicit def doubleNegationIntroduction[A, B](implicit p1: A ==> B): A ==> Not[Not[B]] =
    p1.adapt("doubleNegationIntroduction(%s)")

  implicit def conjunctionAssociativity[A, B, C]: ((A And B) And C) ==> (A And (B And C)) =
    InferenceRule.alwaysValid("conjunctionAssociativity")

  implicit def conjunctionCommutativity[A, B]: (A And B) ==> (B And A) =
    InferenceRule.alwaysValid("conjunctionCommutativity")

  implicit def conjunctionEliminationR[A, B, C](implicit p1: B ==> C): (A And B) ==> C =
    p1.adapt("conjunctionEliminationR(%s)")

  implicit def disjunctionAssociativity[A, B, C]: ((A Or B) Or C) ==> (A Or (B Or C)) =
    InferenceRule.alwaysValid("disjunctionAssociativity")

  implicit def disjunctionCommutativity[A, B]: (A Or B) ==> (B Or A) =
    InferenceRule.alwaysValid("disjunctionCommutativity")

  implicit def disjunctionIntroductionL[A, B]: A ==> (A Or B) =
    InferenceRule.alwaysValid("disjunctionIntroductionL")

  implicit def disjunctionIntroductionR[A, B]: B ==> (A Or B) =
    InferenceRule.alwaysValid("disjunctionIntroductionR")

  implicit def deMorgansLaw1[A, B]: Not[A And B] ==> (Not[A] Or Not[B]) =
    InferenceRule.alwaysValid("deMorgansLaw1")

  implicit def deMorgansLaw2[A, B]: Not[A Or B] ==> (Not[A] And Not[B]) =
    InferenceRule.alwaysValid("deMorgansLaw2")

  implicit def xorCommutativity[A, B]: (A Xor B) ==> (B Xor A) =
    InferenceRule.alwaysValid("xorCommutativity")

  implicit def modusTollens[A, B](implicit p1: A ==> B): Not[B] ==> Not[A] =
    p1.adapt("modusTollens(%s)")
}

private[refined] trait BooleanInferenceRules1 {

  implicit def conjunctionEliminationL[A, B, C](implicit p1: A ==> C): (A And B) ==> C =
    p1.adapt("conjunctionEliminationL(%s)")

  implicit def hypotheticalSyllogism[A, B, C](implicit p1: A ==> B, p2: B ==> C): A ==> C =
    InferenceRule.combine(p1, p2, "hypotheticalSyllogism(%s, %s)")
}
