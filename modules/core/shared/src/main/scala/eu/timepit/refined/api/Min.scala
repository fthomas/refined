package eu.timepit.refined.api

import eu.timepit.refined.boolean.{And, Not}
import eu.timepit.refined.internal.Adjacent
import eu.timepit.refined.numeric.{Greater, Less}
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
trait MinInstances {
  implicit val byteMin: Min[Byte] = Min.instance(Byte.MinValue)
  implicit val shortMin: Min[Short] = Min.instance(Short.MinValue)
  implicit val intMin: Min[Int] = Min.instance(Int.MinValue)
  implicit val longMin: Min[Long] = Min.instance(Long.MinValue)
  implicit val floatMin: Min[Float] = Min.instance(Float.MinValue)
  implicit val doubleMin: Min[Double] = Min.instance(Double.MinValue)
  implicit val charMin: Min[Char] = Min.instance(Char.MinValue)

  implicit def lessMin[F[_, _], T, N](implicit rt: RefType[F], m: Min[T]): Min[F[T, Less[N]]] =
    Min.instance(rt.unsafeWrap[T, Less[N]](m.min))

  implicit def notGreaterMin[F[_, _], T, N](implicit rt: RefType[F],
                                            m: Min[T]): Min[F[T, Not[Greater[N]]]] =
    Min.instance(rt.unsafeWrap[T, Not[Greater[N]]](m.min))

  implicit def notLessMinWit[F[_, _], T, N <: T](implicit rt: RefType[F],
                                                 w: Witness.Aux[N]): Min[F[T, Not[Less[N]]]] =
    Min.instance(rt.unsafeWrap[T, Not[Less[N]]](w.value))

  implicit def notLessMinNat[F[_, _], T, N <: Nat](implicit rt: RefType[F],
                                                   toInt: ToInt[N],
                                                   w: Witness.Aux[N],
                                                   numeric: Numeric[T]): Min[F[T, Not[Less[N]]]] =
    Min.instance(rt.unsafeWrap[T, Not[Less[N]]](numeric.fromInt(toInt.apply())))

  implicit def greaterMin[F[_, _], T, N](implicit rt: RefType[F],
                                         notLessMin: Min[F[T, Not[Less[N]]]],
                                         adj: Adjacent[T]): Min[F[T, Greater[N]]] =
    Min.instance(rt.unsafeWrap[T, Greater[N]](adj.nextUp(rt.unwrap(notLessMin.min))))

  implicit def andMin[F[_, _], T, L, R](implicit rt: RefType[F],
                                        leftMin: Min[F[T, L]],
                                        rightMin: Min[F[T, R]],
                                        numeric: Numeric[T]): Min[F[T, (L And R)]] =
    Min.instance(
      rt.unsafeWrap[T, (L And R)](numeric.max(rt.unwrap(leftMin.min), rt.unwrap(rightMin.min))))
}
