package eu.timepit.refined.scalacheck.util

/**
 * Auxiliary type class that provides the upper and lower bounds of a
 * type `T`. This is needed for the `Arbitrary` instances of numeric
 * refined types.
 */
trait Bounded[T] {
  def minValue: T
  def maxValue: T
}

object Bounded {
  def apply[T](implicit b: Bounded[T]): Bounded[T] = b

  def instance[T](min: T, max: T): Bounded[T] =
    new Bounded[T] {
      val minValue = min
      val maxValue = max
    }

  implicit val byte: Bounded[Byte] =
    instance(Byte.MinValue, Byte.MaxValue)

  implicit val char: Bounded[Char] =
    instance(Char.MinValue, Char.MaxValue)

  implicit val double: Bounded[Double] =
    instance(Double.MinValue, Double.MaxValue)

  implicit val float: Bounded[Float] =
    instance(Float.MinValue, Float.MaxValue)

  implicit val int: Bounded[Int] =
    instance(Int.MinValue, Int.MaxValue)

  implicit val long: Bounded[Long] =
    instance(Long.MinValue, Long.MaxValue)

  implicit val short: Bounded[Short] =
    instance(Short.MinValue, Short.MaxValue)
}
