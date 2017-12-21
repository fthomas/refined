package eu.timepit.refined.scalaz

import eu.timepit.refined.api.{Refined, RefinedType, RefinedTypeOps}
import scalaz.{Validation, ValidationNel}

object validation {
  implicit class ScalazValidateCompanionOps[FTP, T](companion: RefinedTypeOps[FTP, T]) {
    def validate(value: T): ValidationNel[String, FTP] =
      Validation.fromEither(companion.from(value)).toValidationNel
  }
  implicit class ScalazValidateSyntax[T](value: T) {
    def validate[P](
        implicit refined: RefinedType.AuxT[T Refined P, T]): ValidationNel[String, T Refined P] =
      Validation.fromEither(refined.refine(value)).toValidationNel
  }
}
