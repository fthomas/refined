package eu.timepit.refined.api

import eu.timepit.refined.boolean.{And, Not}
import eu.timepit.refined.internal.Adjacent
import eu.timepit.refined.numeric.{Greater, Less}
import shapeless.{Nat, Witness}
import shapeless.ops.nat.ToInt

/**
 * Typeclass defining the maximum value of a given type
 */
trait Max[T] { def max: T }
object Max extends MaxInstances {
  def apply[T](implicit ev: Max[T]): Max[T] = ev
  def instance[T](m: T): Max[T] = new Max[T] { def max: T = m }
}
trait MaxInstances extends LowPriorityMaxInstances {
  implicit val byteMax: Max[Byte] = Max.instance(Byte.MaxValue)
  implicit val shortMax: Max[Short] = Max.instance(Short.MaxValue)
  implicit val intMax: Max[Int] = Max.instance(Int.MaxValue)
  implicit val longMax: Max[Long] = Max.instance(Long.MaxValue)
  implicit val floatMax: Max[Float] = Max.instance(Float.MaxValue)
  implicit val doubleMax: Max[Double] = Max.instance(Double.MaxValue)
  implicit val charMax: Max[Char] = Max.instance(Char.MaxValue)

  implicit def greaterMax[F[_, _], T, N](implicit rt: RefType[F],
                                         m: Max[T]): Max[F[T, Greater[N]]] =
    Max.instance(rt.unsafeWrap(m.max))

  implicit def notLessMax[F[_, _], T, N](implicit rt: RefType[F],
                                         m: Max[T]): Max[F[T, Not[Less[N]]]] =
    Max.instance(rt.unsafeWrap(m.max))

  implicit def notGreaterWit[F[_, _], T, N <: T](implicit rt: RefType[F],
                                                 w: Witness.Aux[N]): Max[F[T, Not[Greater[N]]]] =
    Max.instance(rt.unsafeWrap(w.value))

  implicit def notGreaterNat[F[_, _], T, N <: Nat](
      implicit
      rt: RefType[F],
      toInt: ToInt[N],
      numeric: Numeric[T]): Max[F[T, Not[Greater[N]]]] =
    Max.instance(rt.unsafeWrap(numeric.fromInt(toInt.apply())))

  implicit def lessMax[F[_, _], T, N](implicit rt: RefType[F],
                                      notGreater: Max[F[T, Not[Greater[N]]]],
                                      adj: Adjacent[T]): Max[F[T, Less[N]]] =
    Max.instance(rt.unsafeWrap(adj.nextDown(rt.unwrap(notGreater.max))))

  implicit def andMax[F[_, _], T, L, R](implicit rt: RefType[F],
                                        leftMax: Max[F[T, L]],
                                        rightMax: Max[F[T, R]],
                                        validate: Validate[T, (L And R)],
                                        ordering: Ordering[T],
                                        adjacent: Adjacent[T]): Max[F[T, (L And R)]] =
    Max.instance(
      rt.unsafeWrap(findValid(ordering.min(rt.unwrap(leftMax.max), rt.unwrap(rightMax.max)))))
}
trait LowPriorityMaxInstances {
  implicit def validateMax[F[_, _], T, P](implicit rt: RefType[F],
                                          m: Max[T],
                                          a: Adjacent[T],
                                          v: Validate[T, P]): Max[F[T, P]] =
    Max.instance(rt.unsafeWrap(findValid(m.max)))

  protected def findValid[T, P](from: T)(implicit v: Validate[T, P], a: Adjacent[T]) = {
    var result = from
    while (!v.isValid(result)) result = a.nextDown(result)
    result
  }
}
