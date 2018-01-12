package eu.timepit.refined.api

import eu.timepit.refined.boolean.And
import eu.timepit.refined.internal.Adjacent
import eu.timepit.refined.numeric.{Greater, GreaterEqual, Less, LessEqual}
import shapeless.{Nat, Witness}
import shapeless.ops.nat.ToInt

/**
 * Typeclass defining the minimum value of a given type
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

  implicit def lessMin[F[_, _], T, N](implicit rt: RefType[F], m: Min[T]): Min[F[T, Less[N]]] =
    Min.instance(rt.unsafeWrap(m.min))

  implicit def lessEqualMin[F[_, _], T, N](implicit rt: RefType[F],
                                           m: Min[T]): Min[F[T, LessEqual[N]]] =
    Min.instance(rt.unsafeWrap(m.min))

  implicit def greaterEqualMinWit[F[_, _], T, N <: T](
      implicit rt: RefType[F],
      w: Witness.Aux[N]): Min[F[T, GreaterEqual[N]]] =
    Min.instance(rt.unsafeWrap(w.value))

  implicit def greaterEqualMinNat[F[_, _], T, N <: Nat](
      implicit rt: RefType[F],
      toInt: ToInt[N],
      numeric: Numeric[T]): Min[F[T, GreaterEqual[N]]] =
    Min.instance(rt.unsafeWrap(numeric.fromInt(toInt.apply())))

  implicit def greaterMin[F[_, _], T, N](implicit rt: RefType[F],
                                         greaterEqualMin: Min[F[T, GreaterEqual[N]]],
                                         adj: Adjacent[T]): Min[F[T, Greater[N]]] =
    Min.instance(rt.unsafeWrap(adj.nextUp(rt.unwrap(greaterEqualMin.min))))

  implicit def andMin[F[_, _], T, L, R](implicit rt: RefType[F],
                                        leftMin: Min[F[T, L]],
                                        rightMin: Min[F[T, R]],
                                        validate: Validate[T, (L And R)],
                                        ordering: Ordering[T],
                                        adjacent: Adjacent[T]): Min[F[T, (L And R)]] =
    Min.instance(
      rt.unsafeWrap(findValid(ordering.max(rt.unwrap(leftMin.min), rt.unwrap(rightMin.min)))))
}
trait LowPriorityMinInstances {
  implicit def validateMin[F[_, _], T, P](implicit rt: RefType[F],
                                          m: Min[T],
                                          a: Adjacent[T],
                                          v: Validate[T, P]): Min[F[T, P]] =
    Min.instance(rt.unsafeWrap(findValid(m.min)))

  protected def findValid[T, P](from: T)(implicit v: Validate[T, P], a: Adjacent[T]) =
    Stream.iterate(from)(a.nextUp).filter(v.isValid).head
}
