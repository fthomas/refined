package eu.timepit.refined.api

import eu.timepit.refined.boolean.And
import eu.timepit.refined.internal.Adjacent
import eu.timepit.refined.numeric.{Greater, GreaterEqual, Less, LessEqual}
import shapeless.{Nat, Witness}
import shapeless.ops.nat.ToInt

/**
 * Type class defining the maximum value of a given type
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

  implicit def greaterMax[F[_, _], T, N](
      implicit rt: RefType[F],
      mt: Max[T]
  ): Max[F[T, Greater[N]]] =
    Max.instance(rt.unsafeWrap(mt.max))

  implicit def greaterEqualMax[F[_, _], T, N](
      implicit rt: RefType[F],
      mt: Max[T]
  ): Max[F[T, GreaterEqual[N]]] =
    Max.instance(rt.unsafeWrap(mt.max))

  implicit def lessEqualMaxWit[F[_, _], T, N <: T](
      implicit rt: RefType[F],
      wn: Witness.Aux[N]
  ): Max[F[T, LessEqual[N]]] =
    Max.instance(rt.unsafeWrap(wn.value))

  implicit def lessEqualMaxNat[F[_, _], T, N <: Nat](
      implicit rt: RefType[F],
      tn: ToInt[N],
      nt: Numeric[T]
  ): Max[F[T, LessEqual[N]]] =
    Max.instance(rt.unsafeWrap(nt.fromInt(tn())))

  implicit def lessMax[F[_, _], T, N](
      implicit rt: RefType[F],
      lessEqualMax: Max[F[T, LessEqual[N]]],
      at: Adjacent[T]
  ): Max[F[T, Less[N]]] =
    Max.instance(rt.unsafeWrap(at.nextDown(rt.unwrap(lessEqualMax.max))))

  implicit def andMax[F[_, _], T, L, R](
      implicit rt: RefType[F],
      ml: Max[F[T, L]],
      mr: Max[F[T, R]],
      at: Adjacent[T],
      v: Validate[T, L And R]
  ): Max[F[T, L And R]] =
    Max.instance(rt.unsafeWrap(findValid(at.min(rt.unwrap(ml.max), rt.unwrap(mr.max)))))
}

trait LowPriorityMaxInstances {
  implicit def validateMax[F[_, _], T, P](
      implicit rt: RefType[F],
      mt: Max[T],
      at: Adjacent[T],
      v: Validate[T, P]
  ): Max[F[T, P]] =
    Max.instance(rt.unsafeWrap(findValid(mt.max)))

  protected def findValid[T, P](from: T)(implicit at: Adjacent[T], v: Validate[T, P]): T = {
    var result = from
    while (!v.isValid(result)) result = at.nextDownOrNone(result).get
    result
  }
}
