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
  def showResult(t: T, r: Res): String
}

object Show extends LowPriorityShowInstances {

  type Aux[T, P, R0] = Show[T, P] { type R = R0 }

  def apply[T, P](implicit s: Show[T, P]): Aux[T, P, s.R] = s
}

trait LowPriorityShowInstances {
  implicit def showFromValidate[T, P, R0](implicit v: Validate.Aux[T, P, R0]): Show.Aux[T, P, R0] =
    new Show[T, P] {
      override type R = R0

      override def show(t: T): String = t.toString

      override def withPlaceholder(x: String): String = ""

      override def showExpr(t: T): String = v.showExpr(t)

      override def showResult(t: T, r: Res): String = v.showResult(t, r)
    }
}
