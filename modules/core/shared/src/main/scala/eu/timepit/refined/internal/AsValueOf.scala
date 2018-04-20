package eu.timepit.refined.internal

import shapeless.{Nat, Witness}
import shapeless.ops.nat.ToInt

trait AsValueOf[A, B] {
  def value: B
}

object AsValueOf {
  def instance[A, B](b: B): AsValueOf[A, B] =
    new AsValueOf[A, B] {
      override def value: B = b
    }

  implicit def natAsValueOf[B, A <: Nat](implicit ta: ToInt[A], nb: Numeric[B]): AsValueOf[A, B] =
    instance(nb.fromInt(ta.apply()))

  implicit def witAsValueOf[B, A <: B](implicit wa: Witness.Aux[A]): AsValueOf[A, B] =
    instance(wa.value)
}
