package eu.timepit.refined.internal

/**
 * Type class that provides the next greater or next smaller value for
 * a given argument.
 */
trait Adjacent[T] extends Ordering[T] {

  /**
   * Returns the next greater value adjacent to `t` or `t` if there is
   * no greater value.
   */
  def nextUp(t: T): T

  /**
   * Returns the next smaller value adjacent to `t` or `t` if there is
   * no smaller value.
   */
  def nextDown(t: T): T

  /**
   * Returns the next greater value adjacent to `t` or `None` if there is
   * no greater value.
   */
  def nextUpOrNone(t: T): Option[T] = {
    val n = nextUp(t)
    if (gt(n, t)) Some(n) else None
  }

  /**
   * Returns the next smaller value adjacent to `t` or `None` if there is
   * no smaller value.
   */
  def nextDownOrNone(t: T): Option[T] = {
    val n = nextDown(t)
    if (lt(n, t)) Some(n) else None
  }
}

object Adjacent {
  def apply[T](implicit at: Adjacent[T]): Adjacent[T] = at

  def instance[T](compareF: (T, T) => Int, nextUpF: T => T, nextDownF: T => T): Adjacent[T] =
    new Adjacent[T] {
      override def compare(x: T, y: T): Int = compareF(x, y)
      override def nextUp(t: T): T = nextUpF(t)
      override def nextDown(t: T): T = nextDownF(t)
    }

  implicit val doubleAdjacent: Adjacent[Double] =
    instance(Ordering.Double.compare, Math.nextUp, Math.nextDown)

  implicit val floatAdjacent: Adjacent[Float] =
    instance(Ordering.Float.compare, Math.nextUp, Math.nextDown)

  implicit def integralAdjacent[T](
      implicit it: Integral[T]
  ): Adjacent[T] =
    instance(
      it.compare,
      t => it.max(it.plus(t, it.one), t),
      t => it.min(it.minus(t, it.one), t)
    )
}
