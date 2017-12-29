package eu.timepit.refined.api

/**
 * Typeclass defining the minimum value of a given type
 */
trait Min[T] { def min: T }
object Min {
  def apply[T](implicit ev: Min[T]): Min[T] = ev
  def instance[T](m: T): Min[T] = new Min[T] { def min: T = m }
}

/**
 * Typeclass defining the maximum value of a given type
 */
trait Max[T] { def max: T }
object Max {
  def apply[T](implicit ev: Max[T]): Max[T] = ev
  def instance[T](m: T): Max[T] = new Max[T] { def max: T = m }
}
