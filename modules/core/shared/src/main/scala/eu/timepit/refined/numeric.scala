package eu.timepit.refined

import eu.timepit.refined.api.{Inference, InhabitantsOf, Refined, Validate}
import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.boolean.{And, Not}
import eu.timepit.refined.numeric.Interval.Closed
import eu.timepit.refined.numeric._
import shapeless.{Nat, Witness}
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
object numeric extends NumericValidate with NumericInference with NumericInhabitantsOf {

  /** Predicate that checks if a numeric value is less than `N`. */
  final case class Less[N](n: N)

  /** Predicate that checks if a numeric value is greater than `N`. */
  final case class Greater[N](n: N)

  /** Predicate that checks if a numeric value modulo `N` is `O`. */
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

  /** Predicate that checks if a numeric value is evenly divisible by `N`. */
  type Divisible[N] = Modulo[N, _0]

  /** Predicate that checks if a numeric value is not evenly divisible by `N`. */
  type NonDivisible[N] = Not[Divisible[N]]

  /** Predicate that checks if a numeric value is evenly divisible by 2. */
  type Even = Divisible[_2]

  /** Predicate that checks if a numeric value is not evenly divisible by 2. */
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
}

private[refined] trait NumericValidate {

  implicit def lessValidateWit[T, N <: T](
      implicit wn: Witness.Aux[N],
      nt: Numeric[T]
  ): Validate.Plain[T, Less[N]] =
    Validate.fromPredicate(t => nt.lt(t, wn.value), t => s"($t < ${wn.value})", Less(wn.value))

  implicit def greaterValidateWit[T, N <: T](
      implicit wn: Witness.Aux[N],
      nt: Numeric[T]
  ): Validate.Plain[T, Greater[N]] =
    Validate.fromPredicate(t => nt.gt(t, wn.value), t => s"($t > ${wn.value})", Greater(wn.value))

  implicit def moduloValidateWit[T, N <: T, O <: T](
      implicit wn: Witness.Aux[N],
      wo: Witness.Aux[O],
      nt: Numeric[T]
  ): Validate.Plain[T, Modulo[N, O]] =
    Validate.fromPredicate(
      t ⇒ nt.toDouble(t) % nt.toDouble(wn.value) == nt.toDouble(wo.value),
      t ⇒ s"($t % ${wn.value} == ${wo.value})",
      Modulo(wn.value, wo.value)
    )

  implicit def lessValidateNat[N <: Nat, T](
      implicit tn: ToInt[N],
      wn: Witness.Aux[N],
      nt: Numeric[T]
  ): Validate.Plain[T, Less[N]] =
    Validate.fromPredicate(t => nt.toDouble(t) < tn(), t => s"($t < ${tn()})", Less(wn.value))

  implicit def greaterValidateNat[N <: Nat, T](
      implicit tn: ToInt[N],
      wn: Witness.Aux[N],
      nt: Numeric[T]
  ): Validate.Plain[T, Greater[N]] =
    Validate.fromPredicate(t => nt.toDouble(t) > tn(), t => s"($t > ${tn()})", Greater(wn.value))

  implicit def moduloValidateNat[N <: Nat, O <: Nat, T](
      implicit tn: ToInt[N],
      to: ToInt[O],
      wn: Witness.Aux[N],
      wo: Witness.Aux[O],
      nt: Numeric[T]
  ): Validate.Plain[T, Modulo[N, O]] =
    Validate.fromPredicate(
      t ⇒ nt.toDouble(t) % tn() == to(),
      t ⇒ s"($t % ${tn()} == ${to()})",
      Modulo(wn.value, wo.value)
    )
}

private[refined] trait NumericInference {

  implicit def lessInferenceWit[C, A <: C, B <: C](
      implicit wa: Witness.Aux[A],
      wb: Witness.Aux[B],
      nc: Numeric[C]
  ): Less[A] ==> Less[B] =
    Inference(nc.lt(wa.value, wb.value), s"lessInferenceWit(${wa.value}, ${wb.value})")

  implicit def greaterInferenceWit[C, A <: C, B <: C](
      implicit wa: Witness.Aux[A],
      wb: Witness.Aux[B],
      nc: Numeric[C]
  ): Greater[A] ==> Greater[B] =
    Inference(nc.gt(wa.value, wb.value), s"greaterInferenceWit(${wa.value}, ${wb.value})")

  implicit def lessInferenceNat[A <: Nat, B <: Nat](
      implicit ta: ToInt[A],
      tb: ToInt[B]
  ): Less[A] ==> Less[B] =
    Inference(ta() < tb(), s"lessInferenceNat(${ta()}, ${tb()})")

  implicit def greaterInferenceNat[A <: Nat, B <: Nat](
      implicit ta: ToInt[A],
      tb: ToInt[B]
  ): Greater[A] ==> Greater[B] =
    Inference(ta() > tb(), s"greaterInferenceNat(${ta()}, ${tb()})")

  implicit def lessInferenceWitNat[C, A <: C, B <: Nat](
      implicit wa: Witness.Aux[A],
      tb: ToInt[B],
      nc: Numeric[C]
  ): Less[A] ==> Less[B] =
    Inference(nc.lt(wa.value, nc.fromInt(tb())), s"lessInferenceWitNat(${wa.value}, ${tb()})")

  implicit def greaterInferenceWitNat[C, A <: C, B <: Nat](
      implicit wa: Witness.Aux[A],
      tb: ToInt[B],
      nc: Numeric[C]
  ): Greater[A] ==> Greater[B] =
    Inference(nc.gt(wa.value, nc.fromInt(tb())), s"greaterInferenceWitNat(${wa.value}, ${tb()})")
}

private[refined] trait NumericInhabitantsOf {

  implicit def intervalClosedInhabitantsOf[C, H <: C, L <: C](
      implicit wl: Witness.Aux[L],
      wh: Witness.Aux[H],
      integral: Integral[C]): InhabitantsOf[C Refined Interval.Closed[L, H]] =
    new InhabitantsOf[C Refined Interval.Closed[L, H]] {
      override def all: Stream[C Refined Closed[L, H]] =
        Stream
          .range(wl.value, integral.plus(wh.value, integral.fromInt(1)))
          .map(Refined.unsafeApply[C, Interval.Closed[L, H]])
    }
}
