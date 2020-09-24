package eu.timepit.refined.internal

import shapeless.{Nat, Witness}
import shapeless.ops.nat.ToInt

/**
 * `WitnessAs[A, B]` provides the singleton value of type `A` in `fst`
 * and `fst` converted to type `B` in `snd`.
 *
 * The purpose of this type class is to write numeric type class
 * instances that work with both literal singleton types and
 * `shapeless.Nat`.
 *
 * Example: {{{
 * scala> import shapeless.nat._5
 *
 * scala> WitnessAs[5, Int]
 * res1: WitnessAs[5, Int] = WitnessAs(5,5)
 *
 * scala> WitnessAs[_5, Int]
 * res2: WitnessAs[_5, Int] = WitnessAs(Succ(),5)
 * }}}
 */
final case class WitnessAs[A, B](fst: A, snd: B)

object WitnessAs {
  def apply[A, B](implicit ev: WitnessAs[A, B]): WitnessAs[A, B] = ev

  implicit def natWitnessAs[B, A <: Nat](implicit
      wa: Witness.Aux[A],
      ta: ToInt[A],
      nb: Numeric[B]
  ): WitnessAs[A, B] =
    WitnessAs(wa.value, nb.fromInt(ta.apply()))

  implicit def singletonWitnessAs[B, A <: B](implicit
      wa: ValueOf[A]
  ): WitnessAs[A, B] =
    WitnessAs(wa.value, wa.value)
}
