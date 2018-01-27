package eu.timepit.refined.api

import eu.timepit.refined.boolean.And
import eu.timepit.refined.internal.Adjacent
import eu.timepit.refined.numeric.{Greater, GreaterEqual, Less, LessEqual}
import shapeless.{Nat, Witness}
import shapeless.ops.nat.ToInt

/**
 * Type class defining the minimum value of a given type
 */
trait Min[T] { def min: T }

object Min extends MinInstances {
  def apply[T](implicit ev: Min[T]): Min[T] = ev
  def instance[T](m: T): Min[T] = new Min[T] { def min: T = m }
}

trait MinInstances extends LowPriorityMinInstances {
  implicit val byteMin: Min[Byte] = Min.instance(Byte.MinValue)
  implicit val shortMin: Min[Short] = Min.instance(Short.MinValue)
  implicit val intMin: Min[Int] = Min.instance(Int.MinValue)
  implicit val longMin: Min[Long] = Min.instance(Long.MinValue)
  implicit val floatMin: Min[Float] = Min.instance(Float.MinValue)
  implicit val doubleMin: Min[Double] = Min.instance(Double.MinValue)
  implicit val charMin: Min[Char] = Min.instance(Char.MinValue)

  implicit def lessMin[F[_, _], T, N](
      implicit rt: RefType[F],
      mt: Min[T]
  ): Min[F[T, Less[N]]] =
    Min.instance(rt.unsafeWrap(mt.min))

  implicit def lessEqualMin[F[_, _], T, N](
      implicit rt: RefType[F],
      mt: Min[T]
  ): Min[F[T, LessEqual[N]]] =
    Min.instance(rt.unsafeWrap(mt.min))

  implicit def greaterEqualMinWit[F[_, _], T, N <: T](
      implicit rt: RefType[F],
      wn: Witness.Aux[N]
  ): Min[F[T, GreaterEqual[N]]] =
    Min.instance(rt.unsafeWrap(wn.value))

  implicit def greaterEqualMinNat[F[_, _], T, N <: Nat](
      implicit rt: RefType[F],
      tn: ToInt[N],
      nt: Numeric[T]
  ): Min[F[T, GreaterEqual[N]]] =
    Min.instance(rt.unsafeWrap(nt.fromInt(tn())))

  implicit def greaterMin[F[_, _], T, N](
      implicit rt: RefType[F],
      greaterEqualMin: Min[F[T, GreaterEqual[N]]],
      at: Adjacent[T]
  ): Min[F[T, Greater[N]]] =
    Min.instance(rt.unsafeWrap(at.nextUp(rt.unwrap(greaterEqualMin.min))))

  implicit def andMin[F[_, _], T, L, R](
      implicit rt: RefType[F],
      ml: Min[F[T, L]],
      mr: Min[F[T, R]],
      ot: Ordering[T],
      at: Adjacent[T],
      v: Validate[T, L And R]
  ): Min[F[T, L And R]] =
    Min.instance(rt.unsafeWrap(findValid(ot.max(rt.unwrap(ml.min), rt.unwrap(mr.min)))))
}

trait LowPriorityMinInstances {
  implicit def validateMin[F[_, _], T, P](
      implicit rt: RefType[F],
      mt: Min[T],
      at: Adjacent[T],
      v: Validate[T, P]
  ): Min[F[T, P]] =
    Min.instance(rt.unsafeWrap(findValid(mt.min)))

  protected def findValid[T, P](from: T)(implicit at: Adjacent[T], v: Validate[T, P]): T = {
    var result = from
    while (!v.isValid(result)) result = at.nextUp(result)
    result
  }
}
