package eu.timepit.refined.internal

import scala.compiletime.erasedValue
import scala.compiletime.ops.int.S

trait ToInt[N <: Int] {
  def apply(): Int
}

object ToInt {
  def apply[N <: Int](implicit toInt: ToInt[N]): ToInt[N] = toInt

  inline implicit def materialize[N <: Int]: ToInt[N] =
    new ToInt[N] { override def apply(): Int = toInt[N] }

  private inline def toInt[N <: Int]: Int =
    inline erasedValue[N] match {
      case _: 0    => 0
      case _: S[n] => toInt[n] + 1
    }
}
