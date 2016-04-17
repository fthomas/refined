package eu.timepit.refined

import eu.timepit.refined.api.{ Inference, Validate }
import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.boolean.{ And, Not }
import eu.timepit.refined.numeric._
import shapeless.{ Nat, Witness }
import shapeless.nat._0
import shapeless.ops.nat.ToInt

/**
 * Module for numeric predicates. Predicates that take type parameters
 * support both shapeless' natural numbers (`Nat`) and numeric singleton
 * types (which are made available by shapeless' `Witness` - abbreviated
 * as [[W]] in refined) which include subtypes of `Int`, `Long`, `Double`,
 * `Char` etc.
 *
 * Example: {{{
 * scala> import eu.timepit.refined.api.Refined
 *      | import eu.timepit.refined.numeric.Greater
 *      | import shapeless.nat._5
 *
 * scala> refineMV[Greater[_5]](10)
 * res1: Int Refined Greater[_5] = 10
 *
 * scala> refineMV[Greater[W.`1.5`.T]](1.6)
 * res2: Double Refined Greater[W.`1.5`.T] = 1.6
 * }}}
 */
object numeric extends NumericValidate with NumericInference {

  /** Predicate that checks if a numeric value is less than `N`. */
  case class Less[N](n: N)

  /** Predicate that checks if a numeric value is greater than `N`. */
  case class Greater[N](n: N)

  /** Predicate that checks if a numeric value is less than or equal to `N`. */
  type LessEqual[N] = Not[Greater[N]]

  /** Predicate that checks if a numeric value is greater than or equal to `N`. */
  type GreaterEqual[N] = Not[Less[N]]

  /** Predicate that checks if a numeric value is positive (> 0). */
  type Positive = Greater[_0]

  /** Predicate that checks if a numeric value is zero or negative (<= 0). */
  type NonPositive = Not[Positive]

  /** Predicate that checks if a numeric value is negative (< 0). */
  type Negative = Less[_0]

  /** Predicate that checks if a numeric value is zero or positive (>= 0). */
  type NonNegative = Not[Negative]

  object Interval {

    /** Predicate that checks if a numeric value is in the interval `(L, H)`. */
    type Open[L, H] = Greater[L] And Less[H]

    /** Predicate that checks if a numeric value is in the interval `(L, H]`. */
    type OpenClosed[L, H] = Greater[L] And LessEqual[H]

    /** Predicate that checks if a numeric value is in the interval `[L, H)`. */
    type ClosedOpen[L, H] = GreaterEqual[L] And Less[H]

    /** Predicate that checks if a numeric value is in the interval `[L, H]`. */
    type Closed[L, H] = GreaterEqual[L] And LessEqual[H]
  }
}

private[refined] trait NumericValidate {

  implicit def lessValidateWit[T, N <: T](
    implicit
    wn: Witness.Aux[N], nt: Numeric[T]
  ): Validate.Plain[T, Less[N]] =
    Validate.fromPredicate(t => nt.lt(t, wn.value), t => s"($t < ${wn.value})", Less(wn.value))

  implicit def greaterValidateWit[T, N <: T](
    implicit
    wn: Witness.Aux[N], nt: Numeric[T]
  ): Validate.Plain[T, Greater[N]] =
    Validate.fromPredicate(t => nt.gt(t, wn.value), t => s"($t > ${wn.value})", Greater(wn.value))

  implicit def lessValidateNat[N <: Nat, T](
    implicit
    tn: ToInt[N], wn: Witness.Aux[N], nt: Numeric[T]
  ): Validate.Plain[T, Less[N]] =
    Validate.fromPredicate(t => nt.toDouble(t) < tn(), t => s"($t < ${tn()})", Less(wn.value))

  implicit def greaterValidateNat[N <: Nat, T](
    implicit
    tn: ToInt[N], wn: Witness.Aux[N], nt: Numeric[T]
  ): Validate.Plain[T, Greater[N]] =
    Validate.fromPredicate(t => nt.toDouble(t) > tn(), t => s"($t > ${tn()})", Greater(wn.value))
}

private[refined] trait NumericInference {

  implicit def lessInferenceWit[C, A <: C, B <: C](
    implicit
    wa: Witness.Aux[A], wb: Witness.Aux[B], nc: Numeric[C]
  ): Less[A] ==> Less[B] =
    Inference(nc.lt(wa.value, wb.value), s"lessInferenceWit(${wa.value}, ${wb.value})")

  implicit def greaterInferenceWit[C, A <: C, B <: C](
    implicit
    wa: Witness.Aux[A], wb: Witness.Aux[B], nc: Numeric[C]
  ): Greater[A] ==> Greater[B] =
    Inference(nc.gt(wa.value, wb.value), s"greaterInferenceWit(${wa.value}, ${wb.value})")

  implicit def lessInferenceNat[A <: Nat, B <: Nat](
    implicit
    ta: ToInt[A], tb: ToInt[B]
  ): Less[A] ==> Less[B] =
    Inference(ta() < tb(), s"lessInferenceNat(${ta()}, ${tb()})")

  implicit def greaterInferenceNat[A <: Nat, B <: Nat](
    implicit
    ta: ToInt[A], tb: ToInt[B]
  ): Greater[A] ==> Greater[B] =
    Inference(ta() > tb(), s"greaterInferenceNat(${ta()}, ${tb()})")

  implicit def lessInferenceWitNat[C, A <: C, B <: Nat](
    implicit
    wa: Witness.Aux[A], tb: ToInt[B], nc: Numeric[C]
  ): Less[A] ==> Less[B] =
    Inference(nc.lt(wa.value, nc.fromInt(tb())), s"lessInferenceWitNat(${wa.value}, ${tb()})")

  implicit def greaterInferenceWitNat[C, A <: C, B <: Nat](
    implicit
    wa: Witness.Aux[A], tb: ToInt[B], nc: Numeric[C]
  ): Greater[A] ==> Greater[B] =
    Inference(nc.gt(wa.value, nc.fromInt(tb())), s"greaterInferenceWitNat(${wa.value}, ${tb()})")
}
