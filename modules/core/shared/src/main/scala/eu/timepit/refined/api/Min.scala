package eu.timepit.refined.api

trait Min[T] { def min: T }
object Min {
  def apply[T](implicit ev: Min[T]): Min[T] = ev
  def instance[T](m: T): Min[T] = new Min[T] { def min: T = m }
}

trait Max[T] { def max: T }
object Max {
  def apply[T](implicit ev: Max[T]): Max[T] = ev
  def instance[T](m: T): Max[T] = new Max[T] { def max: T = m }
}

/**
 * Auxiliary type class that provides the next smaller or next greater
 * value for a given argument. This is needed for the `Arbitrary`
 * instances of numeric refined types.
 */
trait Adjacent[T] {
  def nextUp(t: T): T
  def nextDown(t: T): T
}

object Adjacent {
  def apply[T](implicit a: Adjacent[T]): Adjacent[T] = a

  def instance[T](nextUpF: T => T, nextDownF: T => T): Adjacent[T] =
    new Adjacent[T] {
      override def nextUp(t: T): T = nextUpF(t)
      override def nextDown(t: T): T = nextDownF(t)
    }

  implicit val doubleAdjacent: Adjacent[Double] =
    instance(
      t => Math.nextAfter(t, Double.PositiveInfinity),
      t => Math.nextAfter(t, Double.NegativeInfinity)
    )

  implicit val floatAdjacent: Adjacent[Float] =
    instance(
      t => Math.nextAfter(t, Float.PositiveInfinity),
      t => Math.nextAfter(t, Float.NegativeInfinity)
    )

  implicit def numericAdjacent[T](implicit nt: Numeric[T]): Adjacent[T] =
    instance(
      t => nt.plus(t, nt.one),
      t => nt.minus(t, nt.one)
    )
}
