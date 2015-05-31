package eu.timepit.refined

import eu.timepit.refined.boolean._
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.internal.WeakWitness
import shapeless.Nat
import shapeless.nat._
import shapeless.ops.nat.ToInt

object numeric {
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

  /** Predicate that checks if a numeric value is negative (< 0). */
  type Negative = Less[_0]

  /** Predicate that checks if a numeric value is in the interval `[L, H]`. */
  type Interval[L, H] = GreaterEqual[L] And LessEqual[H]

  // Predicate instances

  implicit def lessPredicate[T, N <: T](implicit wn: WeakWitness.Aux[N], nt: Numeric[T]): Predicate[Less[N], T] =
    Predicate.instance(t => nt.lt(t, wn.value), t => s"($t < ${wn.value})")

  implicit def greaterPredicate[T, N <: T](implicit wn: WeakWitness.Aux[N], nt: Numeric[T]): Predicate[Greater[N], T] =
    Predicate.instance(t => nt.gt(t, wn.value), t => s"($t > ${wn.value})")

  implicit def lessPredicateNat[N <: Nat, T](implicit tn: ToInt[N], nt: Numeric[T]): Predicate[Less[N], T] =
    Predicate.instance(t => nt.toDouble(t) < tn.apply(), t => s"($t < ${tn.apply()})")

  implicit def greaterPredicateNat[N <: Nat, T](implicit tn: ToInt[N], nt: Numeric[T]): Predicate[Greater[N], T] =
    Predicate.instance(t => nt.toDouble(t) > tn.apply(), t => s"($t > ${tn.apply()})")

  implicit def equalPredicateNat[N <: Nat, T](implicit tn: ToInt[N], it: Integral[T]): Predicate[Equal[N], T] =
    Predicate.instance(t => it.equiv(t, it.fromInt(tn.apply())), t => s"($t == ${tn.apply()})")

  // Inference instances

  implicit def greaterInference[A <: Nat, B <: Nat](implicit ta: ToInt[A], tb: ToInt[B]): Inference[Greater[A], Greater[B]] =
    Inference.instance(ta.apply() > tb.apply())
}
