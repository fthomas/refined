package eu.timepit.refined.cats

import cats.data.ValidatedNel
import cats.syntax.either._
import eu.timepit.refined.api.RefinedTypeOps

object validation {
  implicit class CatsValidateOps[FTP, T](companion: RefinedTypeOps[FTP, T]) {
    def validate(value: T): ValidatedNel[String, FTP] = companion.from(value).toValidatedNel
  }
}
