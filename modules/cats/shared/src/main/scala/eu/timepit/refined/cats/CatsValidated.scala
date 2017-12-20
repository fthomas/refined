package eu.timepit.refined.cats

import cats.data.{NonEmptyList, Validated, ValidatedNel}
import eu.timepit.refined.api.RefinedTypeOps

object validation {
  implicit class CatsValidateOps[FTP, T](companion: RefinedTypeOps[FTP, T]) {
    def validate(value: T): ValidatedNel[String, FTP] =
      Validated.fromEither(companion.from(value)).leftMap(NonEmptyList.one)
  }
}
