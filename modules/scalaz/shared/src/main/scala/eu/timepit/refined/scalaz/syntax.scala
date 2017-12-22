package eu.timepit.refined.scalaz

import eu.timepit.refined.api.RefinedTypeOps
import scalaz.{Validation, ValidationNel}

object syntax extends ScalazRefinedTypeOpsSyntax

trait ScalazRefinedTypeOpsSyntax {
  implicit class ScalazRefinedTypeOps[FTP, T](companion: RefinedTypeOps[FTP, T]) {
    def validate(t: T): ValidationNel[String, FTP] =
      Validation.fromEither(companion.from(t)).toValidationNel
  }
}
