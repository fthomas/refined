package eu.timepit.refined

import eu.timepit.refined.boolean._
import eu.timepit.refined.generic.Equal
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

  implicit def lessPredicate[N <: Nat, T](implicit tn: ToInt[N], nt: Numeric[T]): Predicate[Less[N], T] =
    Predicate.instance(t => nt.toDouble(t) < tn.apply(), t => s"($t < ${tn.apply()})")

  implicit def greaterPredicate[N <: Nat, T](implicit tn: ToInt[N], nt: Numeric[T]): Predicate[Greater[N], T] =
    Predicate.instance(t => nt.toDouble(t) > tn.apply(), t => s"($t > ${tn.apply()})")

  implicit def equalPredicateNat[N <: Nat, T](implicit tn: ToInt[N], it: Integral[T]): Predicate[Equal[N], T] =
    Predicate.instance(t => it.equiv(t, it.fromInt(tn.apply())), t => s"($t == ${tn.apply()})")
}
