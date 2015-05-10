package refined

import refined.boolean.Not
import shapeless.Nat
import shapeless.nat._
import shapeless.ops.nat.ToInt

object numeric {
  final case class Less[N]()

  implicit def lessPredicate[N <: Nat, X](implicit nt: ToInt[N], nx: Numeric[X]): Predicate[Less[N], X] =
    new Predicate[Less[N], X] {
      def validate(p: Less[N], x: X): Option[String] = {
        val n = nt.apply()
        if (nx.toDouble(x) < n) None
        else Some(s"$x is not less than $n")
      }
    }

  final case class Greater[N]()

  implicit def greaterThanPredicate[N <: Nat, X](implicit nt: ToInt[N], nx: Numeric[X]): Predicate[Greater[N], X] =
    new Predicate[Greater[N], X] {
      def validate(p: Greater[N], x: X): Option[String] = {
        val n = nt.apply()
        if (nx.toDouble(x) > n) None
        else Some(s"$x is not greater than $n")
      }
    }

  type LessEqual[N] = Not[Greater[N]]
  def LessEqual[N]: LessEqual[N] = Not(Greater[N]())

  type GreaterEqual[N] = Not[Less[N]]
  def GreaterEqual[N]: GreaterEqual[N] = Not(Less[N]())

  type Positive = Greater[_0]

  type Negative = Less[_0]
}
