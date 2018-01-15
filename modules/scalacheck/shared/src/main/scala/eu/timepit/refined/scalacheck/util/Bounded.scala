package eu.timepit.refined.scalacheck.util

/**
 * Auxiliary type class that provides the upper and lower bounds of a
 * type `T`. This is needed for the `Arbitrary` instances of numeric
 * refined types.
 */
@deprecated("Use eu.timepit.refined.api.Min, eu.timepit.refined.api.Max", "0.8.6")
trait Bounded[T] {
  def minValue: T
  def maxValue: T
}

@deprecated("Use eu.timepit.refined.api.Min, eu.timepit.refined.api.Max", "0.8.6")
object Bounded {
  @deprecated("Use eu.timepit.refined.api.Min, eu.timepit.refined.api.Max", "0.8.6")
  def apply[T](implicit b: Bounded[T]): Bounded[T] = b

  @deprecated("Use eu.timepit.refined.api.Min, eu.timepit.refined.api.Max", "0.8.6")
  def instance[T](min: T, max: T): Bounded[T] =
    new Bounded[T] {
      val minValue = min
      val maxValue = max
    }

  @deprecated("Use eu.timepit.refined.api.Min, eu.timepit.refined.api.Max", "0.8.6")
  implicit val byte: Bounded[Byte] =
    instance(Byte.MinValue, Byte.MaxValue)

  @deprecated("Use eu.timepit.refined.api.Min, eu.timepit.refined.api.Max", "0.8.6")
  implicit val char: Bounded[Char] =
    instance(Char.MinValue, Char.MaxValue)

  @deprecated("Use eu.timepit.refined.api.Min, eu.timepit.refined.api.Max", "0.8.6")
  implicit val double: Bounded[Double] =
    instance(Double.MinValue, Double.MaxValue)

  @deprecated("Use eu.timepit.refined.api.Min, eu.timepit.refined.api.Max", "0.8.6")
  implicit val float: Bounded[Float] =
    instance(Float.MinValue, Float.MaxValue)

  @deprecated("Use eu.timepit.refined.api.Min, eu.timepit.refined.api.Max", "0.8.6")
  implicit val int: Bounded[Int] =
    instance(Int.MinValue, Int.MaxValue)

  @deprecated("Use eu.timepit.refined.api.Min, eu.timepit.refined.api.Max", "0.8.6")
  implicit val long: Bounded[Long] =
    instance(Long.MinValue, Long.MaxValue)

  @deprecated("Use eu.timepit.refined.api.Min, eu.timepit.refined.api.Max", "0.8.6")
  implicit val short: Bounded[Short] =
    instance(Short.MinValue, Short.MaxValue)
}
