package eu.timepit.refined
package smt
package smtlib

trait Sort[T] {
  def asString: String
}

object Sort {
  def apply[T](implicit sort: Sort[T]): Sort[T] =
    sort

  def instance[T](sort: String): Sort[T] =
    new Sort[T] {
      override def asString: String = sort
    }

  implicit val booleanSort: Sort[Boolean] =
    instance("Bool")

  implicit val doubleSort: Sort[Double] =
    instance("Real")

  implicit val floatSort: Sort[Float] =
    instance("Real")

  implicit val intSort: Sort[Int] =
    instance("Int")

  implicit val longSort: Sort[Long] =
    instance("Int")
}
