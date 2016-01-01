package eu.timepit.refined
package smt
package smtlib

trait DefineFun[T] {
  def asString(value: T): String
}

object DefineFun {
  def apply[T](implicit defineFun: DefineFun[T]): DefineFun[T] =
    defineFun

  def defineFun0[T](name: String, value: String)(implicit sort: Sort[T]): String =
    s"(define-fun $name () ${sort.asString} $value)"

  implicit def defaultDefineFun[T: Sort]: DefineFun[T] =
    new DefineFun[T] {
      override def asString(value: T): String =
        defineFun0("x", value.toString)
    }
}
