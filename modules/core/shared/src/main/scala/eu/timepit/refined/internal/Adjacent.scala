package eu.timepit.refined.internal

/**
 * Auxiliary type class that provides the next greater or next smaller
 * value for a given argument.
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

  def findNextUp[T](from: T, p: T => Boolean)(implicit ev: Adjacent[T]): T = {
    var result = from
    while (!p(result)) result = ev.nextUp(result)
    result
  }

  def findNextDown[T](from: T, p: T => Boolean)(implicit ev: Adjacent[T]): T = {
    var result = from
    while (!p(result)) result = ev.nextDown(result)
    result
  }
}
