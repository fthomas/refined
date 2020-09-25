package eu.timepit.refined

import eu.timepit.refined.api.{Inference, Validate}
import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.boolean.{And, Not}
import eu.timepit.refined.internal.WitnessAs
import eu.timepit.refined.numeric._
import shapeless.Nat
import shapeless.ops.nat.ToInt

/**
 * Module for numeric predicates. Predicates that take type parameters
 * support both shapeless' natural numbers (`Nat`) and numeric singleton
 * types (which are made available by shapeless' `Witness` - abbreviated
 * as `[[W]]` in refined) which include subtypes of `Int`, `Long`,
 * `Double`, `Char` etc.
 *
 * Example: {{{
 * scala> import eu.timepit.refined.api.Refined
 *      | import eu.timepit.refined.numeric.Greater
 *      | import shapeless.nat._5
 *
 * scala> refineV[Greater[_5]](10)
 * res1: Either[String, Int Refined Greater[_5]] = Right(10)
 *
 * scala> refineV[Greater[1.5]](1.6)
 * res2: Either[String, Double Refined Greater[1.5]] = Right(1.6)
 * }}}
 *
 * Note: `[[generic.Equal]]` can also be used for numeric types.
 */
object numeric extends NumericInference {

  /** Predicate that checks if a numeric value is less than `N`. */
  final case class Less[N](n: N)

  /** Predicate that checks if a numeric value is greater than `N`. */
  final case class Greater[N](n: N)

  /** Predicate that checks if an integral value modulo `N` is `O`. */
  final case class Modulo[N, O](n: N, o: O)

  /** Predicate that checks if a floating-point number value is not NaN. */
  final case class NonNaN()

  /** Predicate that checks if a numeric value is less than or equal to `N`. */
  type LessEqual[N] = Not[Greater[N]]

  /** Predicate that checks if a numeric value is greater than or equal to `N`. */
  type GreaterEqual[N] = Not[Less[N]]

  /** Predicate that checks if a numeric value is positive (> 0). */
  type Positive = Greater[0]

  /** Predicate that checks if a numeric value is zero or negative (<= 0). */
  type NonPositive = Not[Positive]

  /** Predicate that checks if a numeric value is negative (< 0). */
  type Negative = Less[0]

  /** Predicate that checks if a numeric value is zero or positive (>= 0). */
  type NonNegative = Not[Negative]

  /** Predicate that checks if an integral value is evenly divisible by `N`. */
  type Divisible[N] = Modulo[N, 0]

  /** Predicate that checks if an integral value is not evenly divisible by `N`. */
  type NonDivisible[N] = Not[Divisible[N]]

  /** Predicate that checks if an integral value is evenly divisible by 2. */
  type Even = Divisible[2]

  /** Predicate that checks if an integral value is not evenly divisible by 2. */
  type Odd = Not[Even]

  object Interval {

    /** Predicate that checks if a numeric value is in the interval `(L, H)`. */
    type Open[L, H] = Greater[L] And Less[H]

    /** Predicate that checks if a numeric value is in the interval `(L, H]`. */
    type OpenClosed[L, H] = Greater[L] And LessEqual[H]

    /** Predicate that checks if a numeric value is in the interval `[L, H)`. */
    type ClosedOpen[L, H] = GreaterEqual[L] And Less[H]

    /** Predicate that checks if a numeric value is in the interval `[L, H]`. */
    type Closed[L, H] = GreaterEqual[L] And LessEqual[H]
  }

  object Less {
    implicit def lessValidate[T, N](implicit
        wn: WitnessAs[N, T],
        nt: Numeric[T]
    ): Validate.Plain[T, Less[N]] =
      Validate.fromPredicate(t => nt.lt(t, wn.snd), t => s"($t < ${wn.snd})", Less(wn.fst))
  }

  object Greater {
    implicit def greaterValidate[T, N](implicit
        wn: WitnessAs[N, T],
        nt: Numeric[T]
    ): Validate.Plain[T, Greater[N]] =
      Validate.fromPredicate(t => nt.gt(t, wn.snd), t => s"($t > ${wn.snd})", Greater(wn.fst))
  }

  object Modulo {
    implicit def moduloValidate[T, N, O](implicit
        wn: WitnessAs[N, T],
        wo: WitnessAs[O, T],
        it: Integral[T]
    ): Validate.Plain[T, Modulo[N, O]] =
      Validate.fromPredicate(
        t => it.rem(t, wn.snd) == wo.snd,
        t => s"($t % ${wn.snd} == ${wo.snd})",
        Modulo(wn.fst, wo.fst)
      )
  }

  object NonNaN {
    implicit def floatNonNaNValidate: Validate.Plain[Float, NonNaN] = fromIsNaN(_.isNaN)
    implicit def doubleNonNaNValidate: Validate.Plain[Double, NonNaN] = fromIsNaN(_.isNaN)

    def fromIsNaN[A](isNaN: A => Boolean): Validate.Plain[A, NonNaN] =
      Validate.fromPredicate(x => !isNaN(x), x => s"($x != NaN)", NonNaN())
  }
}

private[refined] trait NumericInference {

  implicit def lessInference[C, A, B](implicit
      wa: WitnessAs[A, C],
      wb: WitnessAs[B, C],
      nc: Numeric[C]
  ): Less[A] ==> Less[B] =
    Inference(nc.lt(wa.snd, wb.snd), s"lessInference(${wa.snd}, ${wb.snd})")

  implicit def lessInferenceNat[A <: Nat, B <: Nat](implicit
      ta: ToInt[A],
      tb: ToInt[B]
  ): Less[A] ==> Less[B] =
    Inference(ta() < tb(), s"lessInferenceNat(${ta()}, ${tb()})")

  implicit def greaterInference[C, A, B](implicit
      wa: WitnessAs[A, C],
      wb: WitnessAs[B, C],
      nc: Numeric[C]
  ): Greater[A] ==> Greater[B] =
    Inference(nc.gt(wa.snd, wb.snd), s"greaterInference(${wa.snd}, ${wb.snd})")

  implicit def greaterInferenceNat[A <: Nat, B <: Nat](implicit
      ta: ToInt[A],
      tb: ToInt[B]
  ): Greater[A] ==> Greater[B] =
    Inference(ta() > tb(), s"greaterInferenceNat(${ta()}, ${tb()})")

  implicit def greaterEqualInference[A]: Greater[A] ==> GreaterEqual[A] =
    Inference.alwaysValid("greaterEqualInference")

  implicit def lessEqualInference[A]: Less[A] ==> LessEqual[A] =
    Inference.alwaysValid("lessEqualInference")
}
