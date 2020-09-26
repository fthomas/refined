package eu.timepit.refined.internal

import scala.compiletime.{constValue, error}
import shapeless.{Nat, Witness}

/**
 * `WitnessAs[A, B]` provides the singleton value of type `A` in `fst`
 * and `fst` converted to type `B` in `snd`.
 *
 * The purpose of this type class is to allow literals of other
 * types than the base type to be used as arguments in numeric
 * predicates.
 *
 * Example: {{{
 * scala> import eu.timepit.refined.refineV
 *      | import eu.timepit.refined.api.Refined
 *      | import eu.timepit.refined.numeric.{Greater, Less}
 *
 * scala> refineV[Greater[2.718]](BigDecimal(3.141))
 * res0: Either[String, BigDecimal Refined Greater[2.718]] = Right(3.141)
 *
 * scala> refineV[Less[1]](0.618)
 * res1: Either[String, Double Refined Less[1]] = Right(0.618)
 * }}}
 */
final case class WitnessAs[A, B](fst: A, snd: B)

object WitnessAs extends WitnessAs1 {
  def apply[A, B](implicit ev: WitnessAs[A, B]): WitnessAs[A, B] = ev

  implicit def natWitnessAs[B, A <: Nat](implicit
      wa: Witness.Aux[A],
      ta: ToInt[A],
      nb: Numeric[B]
  ): WitnessAs[A, B] =
    WitnessAs(wa.value, nb.fromInt(ta.apply()))

  inline implicit def singletonWitnessAs[B, A <: B]: WitnessAs[A, B] = {
    inline val a = constValue[A]
    WitnessAs(a, a)
  }
}

trait WitnessAs1 {
  inline implicit def intWitnessAsByte[A <: Int]: WitnessAs[A, Byte] =
    inline constValue[A] match {
      case a if a >= -128 && a <= 127 => WitnessAs(a, a.toByte)
      case a => error(s"WitnessAs: $a is not in [Byte.MinValue, Byte.MaxValue]")
    }

  inline implicit def intWitnessAsShort[A <: Int]: WitnessAs[A, Short] =
    inline constValue[A] match {
      case a if a >= -32768 && a <= 32767 => WitnessAs(a, a.toShort)
      case a => error(s"WitnessAs: $a is not in [Short.MinValue, Short.MaxValue]")
    }

  inline implicit def intWitnessAsLong[A <: Int]: WitnessAs[A, Long] = {
    inline val a = constValue[A]
    WitnessAs(a, a.toLong)
  }

  inline implicit def intWitnessAsBigInt[A <: Int]: WitnessAs[A, BigInt] = {
    inline val a = constValue[A]
    WitnessAs(a, BigInt(a))
  }

  inline implicit def intWitnessAsFloat[A <: Int]: WitnessAs[A, Float] = {
    inline val a = constValue[A]
    WitnessAs(a, a.toFloat)
  }

  inline implicit def intWitnessAsDouble[A <: Int]: WitnessAs[A, Double] = {
    inline val a = constValue[A]
    WitnessAs(a, a.toDouble)
  }

  inline implicit def intWitnessAsBigDecimal[A <: Int]: WitnessAs[A, BigDecimal] = {
    inline val a = constValue[A]
    WitnessAs(a, BigDecimal(a))
  }

  inline implicit def doubleWitnessAsFloat[A <: Double]: WitnessAs[A, Float] =
    inline constValue[A] match {
      case a if a >= -3.4028235E38 && a <= 3.4028235E38 => WitnessAs(a, a.toFloat)
      case a => error(s"WitnessAs: $a is not in [Float.MinValue, Float.MaxValue]")
    }

  inline implicit def doubleWitnessAsBigDecimal[A <: Double]: WitnessAs[A, BigDecimal] = {
    inline val a = constValue[A]
    WitnessAs(a, BigDecimal(a))
  }
}
