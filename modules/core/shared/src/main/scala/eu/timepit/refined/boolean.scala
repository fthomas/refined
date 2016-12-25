package eu.timepit.refined

import eu.timepit.refined.api._
import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.boolean._
import eu.timepit.refined.internal.Resources
import shapeless.{::, HList, HNil}
import shapeless.ops.hlist.ToList

/** Module for logical predicates. */
object boolean extends BooleanValidate with BooleanInference0 {

  /** Constant predicate that is always `true`. */
  final case class True()

  /** Constant predicate that is always `false`. */
  final case class False()

  /** Negation of the predicate `P`. */
  final case class Not[P](p: P)

  /** Conjunction of the predicates `A` and `B`. */
  final case class And[A, B](a: A, b: B)

  /** Disjunction of the predicates `A` and `B`. */
  final case class Or[A, B](a: A, b: B)

  /** Exclusive disjunction of the predicates `A` and `B`. */
  final case class Xor[A, B](a: A, b: B)

  /** Conjunction of all predicates in `PS`. */
  final case class AllOf[PS](ps: PS)

  /** Disjunction of all predicates in `PS`. */
  final case class AnyOf[PS](ps: PS)

  /** Exclusive disjunction of all predicates in `PS`. */
  final case class OneOf[PS](ps: PS)

  /** Negated conjunction of the predicates `A` and `B`. */
  type Nand[A, B] = Not[A And B]

  /** Negated disjunction of the predicates `A` and `B`. */
  type Nor[A, B] = Not[A Or B]
}

private[refined] trait BooleanValidate {

  implicit def trueValidate[T]: Validate.Plain[T, True] =
    Validate.alwaysPassed(True())

  implicit def falseValidate[T]: Validate.Plain[T, False] =
    Validate.alwaysFailed(False())

  implicit def notValidate[T, P, R](
      implicit v: Validate.Aux[T, P, R]): Validate.Aux[T, Not[P], Not[v.Res]] =
    new Validate[T, Not[P]] {
      override type R = Not[v.Res]

      override def validate(t: T): Res = {
        val r = v.validate(t)
        Result.fromBoolean(r.isFailed, Not(r))
      }

      override def showExpr(t: T): String =
        s"!${v.showExpr(t)}"

      override def showResult(t: T, r: Res): String = {
        val expr = v.showExpr(t)
        val rp = r.detail.p
        rp match {
          case Passed(_) => Resources.showResultNotInnerPassed(expr)
          case Failed(_) => Resources.showResultNotInnerFailed(expr)
        }
      }
    }

  implicit def andValidate[T, A, RA, B, RB](
      implicit va: Validate.Aux[T, A, RA],
      vb: Validate.Aux[T, B, RB]
  ): Validate.Aux[T, A And B, va.Res And vb.Res] =
    new Validate[T, A And B] {
      override type R = va.Res And vb.Res

      override def validate(t: T): Res = {
        val (ra, rb) = (va.validate(t), vb.validate(t))
        Result.fromBoolean(ra.isPassed && rb.isPassed, And(ra, rb))
      }

      override def showExpr(t: T): String =
        s"(${va.showExpr(t)} && ${vb.showExpr(t)})"

      override def showResult(t: T, r: Res): String = {
        val expr = showExpr(t)
        val (ra, rb) = (r.detail.a, r.detail.b)
        (ra, rb) match {
          case (Passed(_), Passed(_)) =>
            Resources.showResultAndBothPassed(expr)
          case (Passed(_), Failed(_)) =>
            Resources.showResultAndRightFailed(expr, vb.showResult(t, rb))
          case (Failed(_), Passed(_)) =>
            Resources.showResultAndLeftFailed(expr, va.showResult(t, ra))
          case (Failed(_), Failed(_)) =>
            Resources.showResultAndBothFailed(expr, va.showResult(t, ra), vb.showResult(t, rb))
        }
      }
    }

  implicit def orValidate[T, A, RA, B, RB](
      implicit va: Validate.Aux[T, A, RA],
      vb: Validate.Aux[T, B, RB]
  ): Validate.Aux[T, A Or B, va.Res Or vb.Res] =
    new Validate[T, A Or B] {
      override type R = va.Res Or vb.Res

      override def validate(t: T): Res = {
        val (ra, rb) = (va.validate(t), vb.validate(t))
        Result.fromBoolean(ra.isPassed || rb.isPassed, Or(ra, rb))
      }

      override def showExpr(t: T): String =
        s"(${va.showExpr(t)} || ${vb.showExpr(t)})"

      override def showResult(t: T, r: Res): String = {
        val expr = showExpr(t)
        val (ra, rb) = (r.detail.a, r.detail.b)
        (ra, rb) match {
          case (Passed(_), Passed(_)) =>
            Resources.showResultOrBothPassed(expr)
          case (Passed(_), Failed(_)) =>
            Resources.showResultOrLeftPassed(expr)
          case (Failed(_), Passed(_)) =>
            Resources.showResultOrRightPassed(expr)
          case (Failed(_), Failed(_)) =>
            Resources.showResultOrBothFailed(expr, va.showResult(t, ra), vb.showResult(t, rb))
        }
      }
    }

  implicit def xorValidate[T, A, RA, B, RB](
      implicit va: Validate.Aux[T, A, RA],
      vb: Validate.Aux[T, B, RB]
  ): Validate.Aux[T, A Xor B, va.Res Xor vb.Res] =
    new Validate[T, A Xor B] {
      override type R = va.Res Xor vb.Res

      override def validate(t: T): Res = {
        val (ra, rb) = (va.validate(t), vb.validate(t))
        Result.fromBoolean(ra.isPassed ^ rb.isPassed, Xor(ra, rb))
      }

      override def showExpr(t: T): String =
        s"(${va.showExpr(t)} ^ ${vb.showExpr(t)})"

      override def showResult(t: T, r: Res): String = {
        val expr = showExpr(t)
        val (ra, rb) = (r.detail.a, r.detail.b)
        (ra, rb) match {
          case (Passed(_), Passed(_)) =>
            Resources.showResultOrBothPassed(expr)
          case (Passed(_), Failed(_)) =>
            Resources.showResultOrLeftPassed(expr)
          case (Failed(_), Passed(_)) =>
            Resources.showResultOrRightPassed(expr)
          case (Failed(_), Failed(_)) =>
            Resources.showResultOrBothFailed(expr, va.showResult(t, ra), vb.showResult(t, rb))
        }
      }
    }

  implicit def allOfHNilValidate[T]: Validate.Plain[T, AllOf[HNil]] =
    Validate.alwaysPassed(AllOf(HList()))

  implicit def allOfHConsValidate[T, PH, RH, PT <: HList, RT <: HList](
      implicit vh: Validate.Aux[T, PH, RH],
      vt: Validate.Aux[T, AllOf[PT], AllOf[RT]]
  ): Validate.Aux[T, AllOf[PH :: PT], AllOf[vh.Res :: RT]] =
    new Validate[T, AllOf[PH :: PT]] {
      override type R = AllOf[vh.Res :: RT]

      override def validate(t: T): Res = {
        val rh = vh.validate(t)
        val rt = vt.validate(t)
        Result.fromBoolean(rh.isPassed && rt.isPassed, AllOf(rh :: rt.detail.ps))
      }

      override def showExpr(t: T): String =
        accumulateShowExpr(t).mkString("(", " && ", ")")

      override def accumulateShowExpr(t: T): List[String] =
        vh.showExpr(t) :: vt.accumulateShowExpr(t)
    }

  implicit def anyOfHNilValidate[T]: Validate.Plain[T, AnyOf[HNil]] =
    Validate.alwaysFailed(AnyOf(HList()))

  implicit def anyOfHConsValidate[T, PH, RH, PT <: HList, RT <: HList](
      implicit vh: Validate.Aux[T, PH, RH],
      vt: Validate.Aux[T, AnyOf[PT], AnyOf[RT]]
  ): Validate.Aux[T, AnyOf[PH :: PT], AnyOf[vh.Res :: RT]] =
    new Validate[T, AnyOf[PH :: PT]] {
      override type R = AnyOf[vh.Res :: RT]

      override def validate(t: T): Res = {
        val rh = vh.validate(t)
        val rt = vt.validate(t)
        Result.fromBoolean(rh.isPassed || rt.isPassed, AnyOf(rh :: rt.detail.ps))
      }

      override def showExpr(t: T): String =
        accumulateShowExpr(t).mkString("(", " || ", ")")

      override def accumulateShowExpr(t: T): List[String] =
        vh.showExpr(t) :: vt.accumulateShowExpr(t)
    }

  implicit def oneOfHNilValidate[T]: Validate.Plain[T, OneOf[HNil]] =
    Validate.alwaysFailed(OneOf(HList()))

  implicit def oneOfHConsValidate[T, PH, RH, PT <: HList, RT <: HList](
      implicit vh: Validate.Aux[T, PH, RH],
      vt: Validate.Aux[T, OneOf[PT], OneOf[RT]],
      toList: ToList[RT, Result[_]]
  ): Validate.Aux[T, OneOf[PH :: PT], OneOf[vh.Res :: RT]] =
    new Validate[T, OneOf[PH :: PT]] {
      override type R = OneOf[vh.Res :: RT]

      override def validate(t: T): Res = {
        val rh = vh.validate(t)
        val rt = vt.validate(t).detail.ps
        val passed = (rh :: toList(rt)).count(_.isPassed) == 1
        Result.fromBoolean(passed, OneOf(rh :: rt))
      }

      override def showExpr(t: T): String =
        accumulateShowExpr(t).mkString("oneOf(", ", ", ")")

      override def accumulateShowExpr(t: T): List[String] =
        vh.showExpr(t) :: vt.accumulateShowExpr(t)
    }
}

private[refined] trait BooleanInference0 extends BooleanInference1 {

  implicit def minimalTautology[A]: A ==> A =
    Inference.alwaysValid("minimalTautology")

  implicit def doubleNegationElimination[A, B](implicit p1: A ==> B): Not[Not[A]] ==> B =
    p1.adapt("doubleNegationElimination(%s)")

  implicit def doubleNegationIntroduction[A, B](implicit p1: A ==> B): A ==> Not[Not[B]] =
    p1.adapt("doubleNegationIntroduction(%s)")

  implicit def conjunctionAssociativity[A, B, C]: ((A And B) And C) ==> (A And (B And C)) =
    Inference.alwaysValid("conjunctionAssociativity")

  implicit def conjunctionCommutativity[A, B]: (A And B) ==> (B And A) =
    Inference.alwaysValid("conjunctionCommutativity")

  implicit def conjunctionEliminationR[A, B, C](implicit p1: B ==> C): (A And B) ==> C =
    p1.adapt("conjunctionEliminationR(%s)")

  implicit def disjunctionAssociativity[A, B, C]: ((A Or B) Or C) ==> (A Or (B Or C)) =
    Inference.alwaysValid("disjunctionAssociativity")

  implicit def disjunctionCommutativity[A, B]: (A Or B) ==> (B Or A) =
    Inference.alwaysValid("disjunctionCommutativity")

  implicit def disjunctionIntroductionL[A, B]: A ==> (A Or B) =
    Inference.alwaysValid("disjunctionIntroductionL")

  implicit def disjunctionIntroductionR[A, B]: B ==> (A Or B) =
    Inference.alwaysValid("disjunctionIntroductionR")

  implicit def deMorgansLaw1[A, B]: Not[A And B] ==> (Not[A] Or Not[B]) =
    Inference.alwaysValid("deMorgansLaw1")

  implicit def deMorgansLaw2[A, B]: Not[A Or B] ==> (Not[A] And Not[B]) =
    Inference.alwaysValid("deMorgansLaw2")

  implicit def xorCommutativity[A, B]: (A Xor B) ==> (B Xor A) =
    Inference.alwaysValid("xorCommutativity")

  implicit def nandCommutativity[A, B]: (A Nand B) ==> (B Nand A) =
    Inference.alwaysValid("nandCommutativity")

  implicit def norCommutativity[A, B]: (A Nor B) ==> (B Nor A) =
    Inference.alwaysValid("norCommutativity")
}

private[refined] trait BooleanInference1 extends BooleanInference2 {

  implicit def modusTollens[A, B](implicit p1: A ==> B): Not[B] ==> Not[A] =
    p1.adapt("modusTollens(%s)")
}

private[refined] trait BooleanInference2 {

  implicit def conjunctionEliminationL[A, B, C](implicit p1: A ==> C): (A And B) ==> C =
    p1.adapt("conjunctionEliminationL(%s)")

  implicit def hypotheticalSyllogism[A, B, C](implicit p1: A ==> B, p2: B ==> C): A ==> C =
    Inference.combine(p1, p2, "hypotheticalSyllogism(%s, %s)")
}
