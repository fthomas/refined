package eu.timepit.refined

import eu.timepit.refined.InferenceRuleAlias.==>
import eu.timepit.refined.boolean._
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric._
import shapeless.nat._
import shapeless.ops.nat.ToInt
import shapeless.{ Nat, Witness }

object numeric extends NumericPredicates with NumericInferenceRules {

  /** Predicate that checks if a numeric value is less than `N`. */
  trait Less[N]

  /** Predicate that checks if a numeric value is greater than `N`. */
  trait Greater[N]

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

  /** Predicate that checks if a numeric value is in the interval `[L, H]`. */
  type Interval[L, H] = GreaterEqual[L] And LessEqual[H]
}

trait NumericPredicates {

  implicit def lessPredicate[T, N <: T](implicit wn: Witness.Aux[N], nt: Numeric[T]): Predicate[Less[N], T] =
    Predicate.instance(t => nt.lt(t, wn.value), t => s"($t < ${wn.value})")

  implicit def greaterPredicate[T, N <: T](implicit wn: Witness.Aux[N], nt: Numeric[T]): Predicate[Greater[N], T] =
    Predicate.instance(t => nt.gt(t, wn.value), t => s"($t > ${wn.value})")

  implicit def lessPredicateNat[N <: Nat, T](implicit tn: ToInt[N], nt: Numeric[T]): Predicate[Less[N], T] =
    Predicate.instance(t => nt.toDouble(t) < tn.apply(), t => s"($t < ${tn.apply()})")

  implicit def greaterPredicateNat[N <: Nat, T](implicit tn: ToInt[N], nt: Numeric[T]): Predicate[Greater[N], T] =
    Predicate.instance(t => nt.toDouble(t) > tn.apply(), t => s"($t > ${tn.apply()})")

  implicit def equalPredicateNat[N <: Nat, T](implicit tn: ToInt[N], it: Integral[T]): Predicate[Equal[N], T] =
    Predicate.instance(t => it.equiv(t, it.fromInt(tn.apply())), t => s"($t == ${tn.apply()})")
}

trait NumericInferenceRules {

  implicit def lessInference[C, A <: C, B <: C](implicit wa: Witness.Aux[A], wb: Witness.Aux[B], nc: Numeric[C]): Less[A] ==> Less[B] =
    InferenceRule(nc.lt(wa.value, wb.value))

  implicit def greaterInference[C, A <: C, B <: C](implicit wa: Witness.Aux[A], wb: Witness.Aux[B], nc: Numeric[C]): Greater[A] ==> Greater[B] =
    InferenceRule(nc.gt(wa.value, wb.value))

  implicit def lessInferenceNat[A <: Nat, B <: Nat](implicit ta: ToInt[A], tb: ToInt[B]): Less[A] ==> Less[B] =
    InferenceRule(ta.apply() < tb.apply())

  implicit def greaterInferenceNat[A <: Nat, B <: Nat](implicit ta: ToInt[A], tb: ToInt[B]): Greater[A] ==> Greater[B] =
    InferenceRule(ta.apply() > tb.apply())
}
