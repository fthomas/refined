package eu.timepit.refined.internal

import shapeless.{Nat, Witness}
import shapeless.ops.nat.ToInt

/**
 * `WitnessAs[A, B]` provides the singleton value of type `A` in `fst`
 * and `fst` converted to type `B` in `snd`.
 *
 * The purpose of this type class is to allow literals of other
 * types than the base type to be used as arguments in numeric
 * predicates.
 *
 * Example: {{{
 * scala> import eu.timepit.refined.{refineV, W}
 *      | import eu.timepit.refined.api.Refined
 *      | import eu.timepit.refined.numeric.{Greater, Less}
 *
 * scala> refineV[Greater[W.`2.718`.T]](BigDecimal(3.141))
 * res0: Either[String, BigDecimal Refined Greater[W.`2.718`.T]] = Right(3.141)
 *
 * scala> refineV[Less[W.`1`.T]](0.618)
 * res1: Either[String, Double Refined Less[W.`1`.T]] = Right(0.618)
 * }}}
 */
final case class WitnessAs[A, B](fst: A, snd: B)

object WitnessAs extends WitnessAs1 {
  def apply[A, B](implicit ev: WitnessAs[A, B]): WitnessAs[A, B] = ev

  @deprecated(
    "Support for shapeless.Nat as arguments for predicates has been deprecated. " +
      "Use Int literals for any base type or Double literals for fractional base types instead.",
    "0.10.0"
  )
  implicit def natWitnessAs[B, A <: Nat](implicit
      wa: Witness.Aux[A],
      ta: ToInt[A],
      nb: Numeric[B]
  ): WitnessAs[A, B] =
    WitnessAs(wa.value, nb.fromInt(ta.apply()))

  implicit def singletonWitnessAs[B, A <: B](implicit
      wa: Witness.Aux[A]
  ): WitnessAs[A, B] =
    WitnessAs(wa.value, wa.value)
}

trait WitnessAs1 {
  implicit def intWitnessAsByte[A <: Int](implicit
      wa: Witness.Aux[A]
  ): WitnessAs[A, Byte] =
    if (wa.value >= Byte.MinValue.toInt && wa.value <= Byte.MaxValue.toInt)
      WitnessAs(wa.value, wa.value.toByte)
    else sys.error(s"WitnessAs: ${wa.value} is not in [Byte.MinValue, Byte.MaxValue]")

  implicit def intWitnessAsShort[A <: Int](implicit
      wa: Witness.Aux[A]
  ): WitnessAs[A, Short] =
    if (wa.value >= Short.MinValue.toInt && wa.value <= Short.MaxValue.toInt)
      WitnessAs(wa.value, wa.value.toShort)
    else sys.error(s"WitnessAs: ${wa.value} is not in [Short.MinValue, Short.MaxValue]")

  implicit def intWitnessAsLong[A <: Int](implicit
      wa: Witness.Aux[A]
  ): WitnessAs[A, Long] =
    WitnessAs(wa.value, wa.value.toLong)

  implicit def intWitnessAsBigInt[A <: Int](implicit
      wa: Witness.Aux[A]
  ): WitnessAs[A, BigInt] =
    WitnessAs(wa.value, BigInt(wa.value))

  implicit def intWitnessAsFloat[A <: Int](implicit
      wa: Witness.Aux[A]
  ): WitnessAs[A, Float] =
    WitnessAs(wa.value, wa.value.toFloat)

  implicit def intWitnessAsDouble[A <: Int](implicit
      wa: Witness.Aux[A]
  ): WitnessAs[A, Double] =
    WitnessAs(wa.value, wa.value.toDouble)

  implicit def intWitnessAsBigDecimal[A <: Int](implicit
      wa: Witness.Aux[A]
  ): WitnessAs[A, BigDecimal] =
    WitnessAs(wa.value, BigDecimal(wa.value))

  implicit def doubleWitnessAsFloat[A <: Double](implicit
      wa: Witness.Aux[A]
  ): WitnessAs[A, Float] =
    if (wa.value >= Float.MinValue.toDouble && wa.value <= Float.MaxValue.toDouble)
      WitnessAs(wa.value, wa.value.toFloat)
    else sys.error(s"WitnessAs: ${wa.value} is not in [Float.MinValue, Float.MaxValue]")

  implicit def doubleWitnessAsBigDecimal[A <: Double](implicit
      wa: Witness.Aux[A]
  ): WitnessAs[A, BigDecimal] =
    WitnessAs(wa.value, BigDecimal(wa.value))
}
