package eu.timepit.refined

import eu.timepit.refined.api.{Inference, Validate}
import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.boolean.{And, Not}
import eu.timepit.refined.internal.AsValueOf
import eu.timepit.refined.numeric._
import shapeless.Nat
import shapeless.nat.{_0, _2}
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
        vn: AsValueOf[N, T],
        nt: Numeric[T]
    ): Validate.Plain[T, Less[N]] =
      Validate.fromPredicate(t => nt.lt(t, vn.snd), t => s"($t < ${vn.snd})", Less(vn.fst))
  }

  object Greater {
    implicit def greaterValidate[T, N](
        implicit
        vn: AsValueOf[N, T],
        nt: Numeric[T]
    ): Validate.Plain[T, Greater[N]] =
      Validate.fromPredicate(t => nt.gt(t, vn.snd), t => s"($t > ${vn.snd})", Greater(vn.fst))
  }

  object Modulo {
    implicit def moduloValidate[T, N, O](
        implicit
        vn: AsValueOf[N, T],
        vo: AsValueOf[O, T],
        it: Integral[T]
    ): Validate.Plain[T, Modulo[N, O]] =
      Validate.fromPredicate(
        t => it.rem(t, vn.snd) == vo.snd,
        t => s"($t % ${vn.snd} == ${vo.snd})",
        Modulo(vn.fst, vo.fst)
      )
  }
}

private[refined] trait NumericInference {

  implicit def lessInference[C, A, B](
      implicit
      wa: AsValueOf[A, C],
      wb: AsValueOf[B, C],
      nc: Numeric[C]
  ): Less[A] ==> Less[B] =
    Inference(nc.lt(wa.snd, wb.snd), s"lessInference(${wa.snd}, ${wb.snd})")

  implicit def lessInferenceNat[A <: Nat, B <: Nat](
      implicit
      ta: ToInt[A],
      tb: ToInt[B]
  ): Less[A] ==> Less[B] =
    Inference(ta() < tb(), s"lessInferenceNat(${ta()}, ${tb()})")

  implicit def greaterInference[C, A, B](
      implicit
      wa: AsValueOf[A, C],
      wb: AsValueOf[B, C],
      nc: Numeric[C]
  ): Greater[A] ==> Greater[B] =
    Inference(nc.gt(wa.snd, wb.snd), s"greaterInference(${wa.snd}, ${wb.snd})")

  implicit def greaterInferenceNat[A <: Nat, B <: Nat](
      implicit
      ta: ToInt[A],
      tb: ToInt[B]
  ): Greater[A] ==> Greater[B] =
    Inference(ta() > tb(), s"greaterInferenceNat(${ta()}, ${tb()})")

  implicit def greaterEqualInference[A]: Greater[A] ==> GreaterEqual[A] =
    Inference(true, "greaterEqualInference")

  implicit def lessEqualInference[A]: Less[A] ==> LessEqual[A] =
    Inference(true, "lessEqualInference")
}
