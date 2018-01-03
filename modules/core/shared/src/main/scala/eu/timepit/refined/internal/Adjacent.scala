package eu.timepit.refined.internal

trait Adjacent[T] {
  def nextUp(t: T): T
  def nextDown(t: T): T

  def findNextUp(from: T, p: T => Boolean): T = {
    var result = from
    while (!p(result)) result = nextUp(result)
    result
  }

  def findNextDown(from: T, p: T => Boolean): T = {
    var result = from
    while (!p(result)) result = nextDown(result)
    result
  }
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
