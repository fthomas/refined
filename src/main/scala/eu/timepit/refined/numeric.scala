package eu.timepit.refined

import eu.timepit.refined.boolean._
import shapeless.Nat
import shapeless.nat._
import shapeless.ops.nat.ToInt

object numeric {
  sealed trait Less[N]

  implicit def lessPredicate[N <: Nat, X](implicit nt: ToInt[N], nx: Numeric[X]): Predicate[Less[N], X] =
    new Predicate[Less[N], X] {
      def validate(x: X): Option[String] = {
        val n = nt.apply()
        if (nx.toDouble(x) < n) None
        else Some(s"$x is not less than $n")
      }
    }

  sealed trait Greater[N]

  implicit def greaterThanPredicate[N <: Nat, X](implicit nt: ToInt[N], nx: Numeric[X]): Predicate[Greater[N], X] =
    new Predicate[Greater[N], X] {
      def validate(x: X): Option[String] = {
        val n = nt.apply()
        if (nx.toDouble(x) > n) None
        else Some(s"$x is not greater than $n")
      }
    }

  type LessEqual[N] = Not[Greater[N]]

  type GreaterEqual[N] = Not[Less[N]]

  type Positive = Greater[_0]

  type Negative = Less[_0]

  type ZeroToOne = GreaterEqual[_0] And LessEqual[_1]
}
