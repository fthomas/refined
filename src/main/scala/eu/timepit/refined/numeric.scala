package eu.timepit.refined

import eu.timepit.refined.boolean._
import shapeless.Nat
import shapeless.nat._
import shapeless.ops.nat.ToInt

object numeric {
  trait Less[N]

  trait Greater[N]

  trait Equal[N]

  type LessEqual[N] = Not[Greater[N]]

  type GreaterEqual[N] = Not[Less[N]]

  type Positive = Greater[_0]

  type Negative = Less[_0]

  type Interval[L, H] = GreaterEqual[L] And LessEqual[H]

  implicit def lessPredicate[N <: Nat, T](implicit tn: ToInt[N], nt: Numeric[T]): Predicate[Less[N], T] =
    Predicate.instance(t => nt.toDouble(t) < tn.apply(), t => s"($t < ${tn.apply()})")

  implicit def greaterPredicate[N <: Nat, T](implicit tn: ToInt[N], nt: Numeric[T]): Predicate[Greater[N], T] =
    Predicate.instance(t => nt.toDouble(t) > tn.apply(), t => s"($t > ${tn.apply()})")

  implicit def equalPredicate[N <: Nat, T](implicit tn: ToInt[N], it: Integral[T]): Predicate[Equal[N], T] =
    Predicate.instance(t => it.equiv(t, it.fromInt(tn.apply())), t => s"($t == ${tn.apply()})")
}
