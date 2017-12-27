package eu.timepit.refined

import eu.timepit.refined.api._
import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.boolean.{And, Not}
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
object numeric extends NumericValidate with NumericInference with NumericMin with NumericMax {

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

  def moduloValidateWit[T, N <: T, O <: T](
      implicit wn: Witness.Aux[N],
      wo: Witness.Aux[O],
      nt: Numeric[T]
  ): Validate.Plain[T, Modulo[N, O]] =
    Validate.fromPredicate(
      t ⇒ nt.toDouble(t) % nt.toDouble(wn.value) == nt.toDouble(wo.value),
      t ⇒ s"($t % ${wn.value} == ${wo.value})",
      Modulo(wn.value, wo.value)
    )

  implicit def moduloValidateWitIntegral[T, N <: T, O <: T](
      implicit wn: Witness.Aux[N],
      wo: Witness.Aux[O],
      nt: Integral[T]
  ): Validate.Plain[T, Modulo[N, O]] =
    Validate.fromPredicate(
      t ⇒ nt.rem(t, wn.value) == wo.value,
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

  def moduloValidateNat[N <: Nat, O <: Nat, T](
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

  implicit def moduloValidateNatIntegral[N <: Nat, O <: Nat, T](
      implicit tn: ToInt[N],
      to: ToInt[O],
      wn: Witness.Aux[N],
      wo: Witness.Aux[O],
      i: Integral[T]
  ): Validate.Plain[T, Modulo[N, O]] =
    Validate.fromPredicate(
      t ⇒ i.rem(t, i.fromInt(tn())) == i.fromInt(to()),
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

private[refined] trait NumericMin {
  implicit val byteMin: Min[Byte] = Min.instance(Byte.MinValue)
  implicit val shortMin: Min[Short] = Min.instance(Short.MinValue)
  implicit val intMin: Min[Int] = Min.instance(Int.MinValue)
  implicit val longMin: Min[Long] = Min.instance(Long.MinValue)
  implicit val floatMin: Min[Float] = Min.instance(Float.MinValue)
  implicit val doubleMin: Min[Double] = Min.instance(Double.MinValue)
  implicit val charMin: Min[Char] = Min.instance(Char.MinValue)

  implicit def lessMin[F[_, _], C, N](implicit rt: RefType[F], m: Min[C]): Min[F[C, Less[N]]] =
    Min.instance(rt.unsafeWrap[C, Less[N]](m.min))

  implicit def notGreaterMin[F[_, _], C, N](implicit rt: RefType[F],
                                            m: Min[C]): Min[F[C, Not[Greater[N]]]] =
    Min.instance(rt.unsafeWrap[C, Not[Greater[N]]](m.min))

  implicit def notLessMinWit[F[_, _], C, N <: C](implicit rt: RefType[F],
                                                 w: Witness.Aux[N]): Min[F[C, Not[Less[N]]]] =
    Min.instance(rt.unsafeWrap[C, Not[Less[N]]](w.value))

  implicit def notLessMinNat[F[_, _], C, N <: Nat](implicit rt: RefType[F],
                                                   toInt: ToInt[N],
                                                   w: Witness.Aux[N],
                                                   numeric: Numeric[C]): Min[F[C, Not[Less[N]]]] =
    Min.instance(rt.unsafeWrap[C, Not[Less[N]]](numeric.fromInt(toInt.apply())))

  implicit def greaterMin[F[_, _], C, N](implicit rt: RefType[F],
                                         notLessMin: Min[F[C, Not[Less[N]]]],
                                         adj: Adjacent[C]): Min[F[C, Greater[N]]] =
    Min.instance(rt.unsafeWrap[C, Greater[N]](adj.nextUp(rt.unwrap(notLessMin.min))))

  implicit def andMin[F[_, _], C, L, R](implicit rt: RefType[F],
                                        leftMin: Min[F[C, L]],
                                        rightMin: Min[F[C, R]],
                                        numeric: Numeric[C]): Min[F[C, (L And R)]] =
    Min.instance(
      rt.unsafeWrap[C, (L And R)](numeric.max(rt.unwrap(leftMin.min), rt.unwrap(rightMin.min))))
}

private[refined] trait NumericMax {
  implicit val byteMax: Max[Byte] = Max.instance(Byte.MaxValue)
  implicit val shortMax: Max[Short] = Max.instance(Short.MaxValue)
  implicit val intMax: Max[Int] = Max.instance(Int.MaxValue)
  implicit val longMax: Max[Long] = Max.instance(Long.MaxValue)
  implicit val floatMax: Max[Float] = Max.instance(Float.MaxValue)
  implicit val doubleMax: Max[Double] = Max.instance(Double.MaxValue)
  implicit val charMax: Max[Char] = Max.instance(Char.MaxValue)

  implicit def greaterMax[F[_, _], C, N](implicit rt: RefType[F],
                                         m: Max[C]): Max[F[C, Greater[N]]] =
    Max.instance(rt.unsafeWrap[C, Greater[N]](m.max))

  implicit def notLessMax[F[_, _], C, N](implicit rt: RefType[F],
                                         m: Max[C]): Max[F[C, Not[Less[N]]]] =
    Max.instance(rt.unsafeWrap[C, Not[Less[N]]](m.max))

  implicit def notGreaterWit[F[_, _], C, N <: C](implicit rt: RefType[F],
                                                 w: Witness.Aux[N]): Max[F[C, Not[Greater[N]]]] =
    Max.instance(rt.unsafeWrap[C, Not[Greater[N]]](w.value))

  implicit def notGreaterNat[F[_, _], C, N <: Nat](
      implicit
      rt: RefType[F],
      toInt: ToInt[N],
      w: Witness.Aux[N],
      numeric: Numeric[C]): Max[F[C, Not[Greater[N]]]] =
    Max.instance(rt.unsafeWrap[C, Not[Greater[N]]](numeric.fromInt(toInt.apply())))

  implicit def lessMax[F[_, _], C, N](implicit rt: RefType[F],
                                      notGreater: Max[F[C, Not[Greater[N]]]],
                                      adj: Adjacent[C]): Max[F[C, Less[N]]] =
    Max.instance(rt.unsafeWrap[C, Less[N]](adj.nextDown(rt.unwrap(notGreater.max))))

  implicit def andMax[F[_, _], C, L, R](implicit rt: RefType[F],
                                        leftMax: Max[F[C, L]],
                                        rightMax: Max[F[C, R]],
                                        numeric: Numeric[C]): Max[F[C, (L And R)]] =
    Max.instance(
      rt.unsafeWrap[C, (L And R)](numeric.min(rt.unwrap(leftMax.max), rt.unwrap(rightMax.max))))
}
