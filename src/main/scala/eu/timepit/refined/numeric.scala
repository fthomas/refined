package eu.timepit.refined

import eu.timepit.refined.boolean._
import shapeless.Nat
import shapeless.nat._
import shapeless.ops.nat.ToInt

object numeric {
  sealed trait Less[N]

  sealed trait Greater[N]

  sealed trait Equal[N]

  type LessEqual[N] = Not[Greater[N]]

  type GreaterEqual[N] = Not[Less[N]]

  type Positive = Greater[_0]

  type Negative = Less[_0]

  type ZeroToOne = GreaterEqual[_0] And LessEqual[_1]

  implicit def lessPredicate[N <: Nat, T](implicit tn: ToInt[N], nt: Numeric[T]): Predicate[Less[N], T] =
    new Predicate[Less[N], T] {
      def isValid(t: T): Boolean = nt.toDouble(t) < tn.apply()
      def show(t: T): String = s"($t < ${tn.apply()})"
    }

  implicit def greaterPredicate[N <: Nat, T](implicit tn: ToInt[N], nt: Numeric[T]): Predicate[Greater[N], T] =
    new Predicate[Greater[N], T] {
      def isValid(t: T): Boolean = nt.toDouble(t) > tn.apply()
      def show(t: T): String = s"($t > ${tn.apply()})"
    }

  implicit def equalPredicate[N <: Nat, T](implicit tn: ToInt[N], it: Integral[T]): Predicate[Equal[N], T] =
    new Predicate[Equal[N], T] {
      def isValid(t: T): Boolean = it.equiv(t, it.fromInt(tn.apply()))
      def show(t: T): String = s"($t == ${tn.apply()})"
    }
}
