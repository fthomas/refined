package eu.timepit.refined.api

import eu.timepit.refined.internal.Resources

trait Show[T, P] extends Serializable {

  type R

  final type Res = Result[R]

  def showExpr(t: T): String

  def showResult(t: T, r: Res): String =
    Resources.predicateResultDetailDot(r, showExpr(t))
}

object Show extends LowPriorityShowInstances {

  type Aux[T, P, R0] = Show[T, P] { type R = R0 }

  def apply[T, P](implicit s: Show[T, P]): Aux[T, P, s.R] = s

  def instance[T, P, R0](showExprF: T => String): Aux[T, P, R0] =
    new Show[T, P] {
      override type R = R0

      override def showExpr(t: T): String = showExprF(t)
    }
}

trait LowPriorityShowInstances {

  implicit def showFromValidate[T, P, R0](implicit v: Validate.Aux[T, P, R0]): Show.Aux[T, P, R0] =
    new Show[T, P] {
      override type R = R0

      override def showExpr(t: T): String = v.showExpr(t)

      override def showResult(t: T, r: Res): String = v.showResult(t, r)
    }
}
