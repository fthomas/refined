package eu.timepit.refined

import _root_.com.monovore.decline.Argument
import cats.data.{ValidatedNel, Validated}
import eu.timepit.refined.api.{RefType, Validate}

package object decline {

  implicit def refTypeArgument[F[_, _], T, P](
      implicit argument: Argument[T],
      refType: RefType[F],
      validate: Validate[T, P]
  ): Argument[F[T, P]] = new Argument[F[T, P]] {

    override def defaultMetavar: String = argument.defaultMetavar

    override def read(string: String): ValidatedNel[String, F[T, P]] =
      argument.read(string) match {
        case Validated.Valid(t) =>
          refType.refine[P](t) match {
            case Left(reason) =>
              Validated.invalidNel(reason)

            case Right(refined) =>
              Validated.validNel(refined)
          }

        case Validated.Invalid(errs) =>
          Validated.invalid(errs)
      }

  }

}
