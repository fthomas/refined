package eu.timepit.refined.internal

import scala.compiletime.erasedValue
import shapeless.{_0, Nat, Succ}

trait ToInt[N <: Nat] {
  def apply(): Int
}

object ToInt {
  def apply[N <: Nat](implicit toInt: ToInt[N]): ToInt[N] = toInt

  inline implicit def materialize[N <: Nat]: ToInt[N] =
    new ToInt[N] { override def apply(): Int = toInt[N] }

  private inline def toInt[N <: Nat]: Int =
    inline erasedValue[N] match {
      case _: _0      => 0
      case _: Succ[n] => toInt[n] + 1
    }
}
