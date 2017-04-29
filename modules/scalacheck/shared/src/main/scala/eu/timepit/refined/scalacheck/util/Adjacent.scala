package eu.timepit.refined.scalacheck.util

/**
 * Auxiliary type class that provides the next smaller or next greater
 * value for a given argument. This is needed for the `Arbitrary`
 * instances of numeric refined types.
 */
trait Adjacent[T] {
  def nextUp(t: T): Option[T]

  def nextDown(t: T): Option[T]

  def nextUpOrSelf(t: T): T =
    nextUp(t).getOrElse(t)

  def nextDownOrSelf(t: T): T =
    nextDown(t).getOrElse(t)
}

object Adjacent {
  def apply[T](implicit a: Adjacent[T]): Adjacent[T] = a

  def instance[T](nextUpF: T => Option[T], nextDownF: T => Option[T]): Adjacent[T] =
    new Adjacent[T] {
      override def nextUp(t: T): Option[T] = nextUpF(t)
      override def nextDown(t: T): Option[T] = nextDownF(t)
    }

  implicit val doubleAdjacent: Adjacent[Double] =
    instance(
      t => firstIfGt(Math.nextAfter(t, Double.PositiveInfinity), t),
      t => firstIfLt(Math.nextAfter(t, Double.NegativeInfinity), t)
    )

  implicit val floatAdjacent: Adjacent[Float] =
    instance(
      t => firstIfGt(Math.nextAfter(t, Float.PositiveInfinity), t),
      t => firstIfLt(Math.nextAfter(t, Float.NegativeInfinity), t)
    )

  implicit def numericAdjacent[T](implicit nt: Numeric[T]): Adjacent[T] =
    instance(
      t => firstIfGt(nt.plus(t, nt.one), t),
      t => firstIfLt(nt.minus(t, nt.one), t)
    )

  private def firstIfGt[T](x: T, y: T)(implicit nt: Numeric[T]): Option[T] =
    if (nt.gt(x, y)) Some(x) else None

  private def firstIfLt[T](x: T, y: T)(implicit nt: Numeric[T]): Option[T] =
    if (nt.lt(x, y)) Some(x) else None
}
