package eu.timepit.refined.scalacheck.util

@deprecated("Bounded has been replaced by eu.timepit.refined.api.Min/Max", "0.9.0")
trait Bounded[T] {
  def minValue: T
  def maxValue: T
}

@deprecated("Bounded has been replaced by eu.timepit.refined.api.Min/Max", "0.9.0")
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
