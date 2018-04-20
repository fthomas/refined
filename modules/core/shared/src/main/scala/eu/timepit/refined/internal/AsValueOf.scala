package eu.timepit.refined.internal

import shapeless.{Nat, Witness}
import shapeless.ops.nat.ToInt

trait AsValueOf[A, B] {
  def fst: A
  def snd: B
}

object AsValueOf {
  def instance[A, B](a: A, b: B): AsValueOf[A, B] =
    new AsValueOf[A, B] {
      override def fst: A = a
      override def snd: B = b
    }

  implicit def natAsValueOf[B, A <: Nat](
      implicit
      wa: Witness.Aux[A],
      ta: ToInt[A],
      nb: Numeric[B]
  ): AsValueOf[A, B] =
    instance(wa.value, nb.fromInt(ta.apply()))

  implicit def witAsValueOf[B, A <: B](
      implicit wa: Witness.Aux[A]
  ): AsValueOf[A, B] =
    instance(wa.value, wa.value)
}
