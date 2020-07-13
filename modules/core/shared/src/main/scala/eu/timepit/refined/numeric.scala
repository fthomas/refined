package eu.timepit.refined

import eu.timepit.refined.api.{Inference, Validate}
import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.boolean.{And, Not}
import eu.timepit.refined.internal.WitnessAs
import eu.timepit.refined.numeric._
//import shapeless.Nat
import shapeless.nat.{_0, _2}
//import shapeless.ops.nat.ToInt
import singleton.ops.{<, >, Id, OpAuxBoolean}

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
 * scala> refineMV[Greater[_5]](10)
 * res1: Int Refined Greater[_5] = 10
 *
 * scala> refineMV[Greater[W.`1.5`.T]](1.6)
 * res2: Double Refined Greater[W.`1.5`.T] = 1.6
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
  type Positive = Greater[_0]

  /** Predicate that checks if a numeric value is zero or negative (<= 0). */
  type NonPositive = Not[Positive]

  /** Predicate that checks if a numeric value is negative (< 0). */
  type Negative = Less[_0]

  /** Predicate that checks if a numeric value is zero or positive (>= 0). */
  type NonNegative = Not[Negative]

  /** Predicate that checks if an integral value is evenly divisible by `N`. */
  type Divisible[N] = Modulo[N, _0]

  /** Predicate that checks if an integral value is not evenly divisible by `N`. */
  type NonDivisible[N] = Not[Divisible[N]]

  /** Predicate that checks if an integral value is evenly divisible by 2. */
  type Even = Divisible[_2]

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
    implicit def lessValidate[T, N](
        implicit
        wn: WitnessAs[N, T],
        nt: Numeric[T]
    ): Validate.Plain[T, Less[N]] =
      Validate.fromPredicate(t => nt.lt(t, wn.snd), t => s"($t < ${wn.snd})", Less(wn.fst))
  }

  object Greater {
    implicit def greaterValidate[T, N](
        implicit
        wn: WitnessAs[N, T],
        nt: Numeric[T]
    ): Validate.Plain[T, Greater[N]] =
      Validate.fromPredicate(t => nt.gt(t, wn.snd), t => s"($t > ${wn.snd})", Greater(wn.fst))
  }

  object Modulo {
    implicit def moduloValidate[T, N, O](
        implicit
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

  implicit def lessInference[A, B](
      implicit
      t: OpAuxBoolean[A < B, W.`true`.T],
      va: Id[A],
      vb: Id[B]
  ): Less[A] ==> Less[B] =
    Inference(s"lessInference(${va.value}, ${vb.value})")

  implicit def greaterInference[A, B](
      implicit
      t: OpAuxBoolean[A > B, W.`true`.T],
      va: Id[A],
      vb: Id[B]
  ): Greater[A] ==> Greater[B] =
    Inference(s"greaterInference(${va.value}, ${vb.value})")

  implicit def greaterEqualInference[A]: Greater[A] ==> GreaterEqual[A] =
    Inference("greaterEqualInference")

  implicit def lessEqualInference[A]: Less[A] ==> LessEqual[A] =
    Inference("lessEqualInference")
}
