package eu.timepit.refined.api

trait Min[FTP] { def min: FTP }
object Min {
  def apply[FTP](implicit ev: Min[FTP]): Min[FTP] = ev
  def instance[FTP](m: FTP): Min[FTP] = new Min[FTP] { def min: FTP = m }
}

trait Max[FTP] { def max: FTP }
object Max {
  def apply[FTP](implicit ev: Max[FTP]): Max[FTP] = ev
  def instance[FTP](m: FTP): Max[FTP] = new Max[FTP] { def max: FTP = m }
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
