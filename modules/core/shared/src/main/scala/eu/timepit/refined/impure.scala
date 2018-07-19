package eu.timepit.refined

import eu.timepit.refined.api.{Failed, Result, Validate}
import eu.timepit.refined.internal.Resources

object impure {

  final case class Null()

  /** Safe evaluation of potentially null values. */
  final case class NonNull[A](a: A)

  object NonNull {
    implicit def nonNullValidate[T, A, R](
        implicit v: Validate.Aux[T, A, R]
    ): Validate.Aux[T, NonNull[A], NonNull[Either[Null, v.Res]]] =
      new Validate[T, NonNull[A]] {
        override type R = NonNull[Either[Null, v.Res]]

        override def validate(t: T): Res =
          if (t == null) Failed(NonNull(Left(Null())))
          else {
            val ra = v.validate(t)
            Result.fromBoolean(ra.isPassed, NonNull(Right(ra)))
          }

        override def showExpr(t: T): String =
          if (t == null) "source is null"
          else if (v.validate(t).isFailed)
            Resources.showResultAndLeftFailed("NonNull passed", v.showExpr(t))
          else Resources.showResultAndBothPassed(s"NonNull and ${v.showExpr(t)}")
      }
  }
}
