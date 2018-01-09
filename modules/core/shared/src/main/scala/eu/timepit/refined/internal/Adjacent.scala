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
      t => Math.nextUp(t),
      t => Math.nextDown(t)
    )

  implicit val floatAdjacent: Adjacent[Float] =
    instance(
      t => Math.nextUp(t),
      t => Math.nextDown(t)
    )

  implicit def integralAdjacent[T](implicit it: Integral[T]): Adjacent[T] =
    instance(
      t => it.max(it.plus(t, it.one), t),
      t => it.min(it.minus(t, it.one), t)
    )

  def findNextUp[T](from: T)(p: T => Boolean)(implicit ev: Adjacent[T]): T = {
    var result = from
    while (!p(result)) result = ev.nextUp(result)
    result
  }

  def findNextDown[T](from: T)(p: T => Boolean)(implicit ev: Adjacent[T]): T = {
    var result = from
    while (!p(result)) result = ev.nextDown(result)
    result
  }
}
