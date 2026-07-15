package eu.timepit.refined.internal

import scala.compiletime.{constValue, error}

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

  @deprecated("Use one of the specialized instances of intWitnessAs*", "0.11.3")
  def intWitnessAs[B, A <: Int](implicit
      wa: ValueOf[A],
      ta: ToInt[A],
      nb: Numeric[B]
  ): WitnessAs[A, B] =
    WitnessAs(wa.value, nb.fromInt(ta.apply()))

  // Uses `ValueOf` rather than `constValue` so the identity case witnesses not only literal constant
  // types (`3`, `"abc"`, ...) but also object singleton types (`Foo.type`) — the compiler synthesizes
  // `ValueOf` for both, whereas `constValue` rejects the latter ("not a constant type"). This matches
  // the Scala 2 `Equal[Foo.type]` behavior backed by shapeless `Witness`.
  //
  // It is `inline` so `v.value` is inlined to a direct selection on the summoned `ValueOf` at the use
  // site, which Hearth's `semiEval` can reduce at compile time (hearth >= 0.4.1). A non-inline given
  // would be invoked reflectively by `semiEval`, and because `scala.ValueOf` is a value class its
  // parameter erases to the underlying value — the boxed `ValueOf` would then leak in as the witnessed
  // value and blow up compile-time refinement with a `ClassCastException`.
  inline given singletonWitnessAs[B, A <: B](using inline v: ValueOf[A]): WitnessAs[A, B] =
    WitnessAs(v.value, v.value)
}

trait WitnessAs1 {
  inline given intWitnessAsByte[A <: Int]: WitnessAs[A, Byte] =
    inline constValue[A] match {
      case a if a >= -128 && a <= 127 => WitnessAs(a, a.toByte)
      case a => error(s"WitnessAs: $a is not in [Byte.MinValue, Byte.MaxValue]")
    }

  inline given intWitnessAsShort[A <: Int]: WitnessAs[A, Short] =
    inline constValue[A] match {
      case a if a >= -32768 && a <= 32767 => WitnessAs(a, a.toShort)
      case a => error(s"WitnessAs: $a is not in [Short.MinValue, Short.MaxValue]")
    }

  inline given intWitnessAsLong[A <: Int]: WitnessAs[A, Long] = {
    inline val a = constValue[A]
    WitnessAs(a, a.toLong)
  }

  inline given intWitnessAsBigInt[A <: Int]: WitnessAs[A, BigInt] = {
    inline val a = constValue[A]
    WitnessAs(a, BigInt(a))
  }

  inline given intWitnessAsFloat[A <: Int]: WitnessAs[A, Float] = {
    inline val a = constValue[A]
    WitnessAs(a, a.toFloat)
  }

  inline given intWitnessAsDouble[A <: Int]: WitnessAs[A, Double] = {
    inline val a = constValue[A]
    WitnessAs(a, a.toDouble)
  }

  inline given intWitnessAsBigDecimal[A <: Int]: WitnessAs[A, BigDecimal] = {
    inline val a = constValue[A]
    WitnessAs(a, BigDecimal(a))
  }

  inline given doubleWitnessAsFloat[A <: Double]: WitnessAs[A, Float] =
    inline constValue[A] match {
      case a if a >= -3.4028235e38 && a <= 3.4028235e38 => WitnessAs(a, a.toFloat)
      case a => error(s"WitnessAs: $a is not in [Float.MinValue, Float.MaxValue]")
    }

  inline given doubleWitnessAsBigDecimal[A <: Double]: WitnessAs[A, BigDecimal] = {
    inline val a = constValue[A]
    WitnessAs(a, BigDecimal(a))
  }
}
