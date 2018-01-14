package eu.timepit.refined.api

trait Show[T, P] {

  type R

  final type Res = Result[R]

  // t: Int = 1 => "1"
  def show(t: T): String

  // x > 0
  def withPlaceholder(x: String): String

  // x > 0 where x = 1
  def showExpr(t: T): String

  // Predicate failed: x > 0 where x = 1
  def showResult[R](t: T, result: Result[R])
}

object Show extends LowPriorityShowInstances {}

trait LowPriorityShowInstances {
  implicit def showFromValidate[T, P](implicit v: Validate[T, P]): Show[T, P] =
    new Show[T, P] {
      override type R = v.R

      override def show(t: T): String = ""

      override def withPlaceholder(x: String): String = ""

      override def showExpr(t: T): String = ""

      override def showResult[R](t: T, result: Result[R]): Unit = ""
    }
}
