package eu.timepit.refined.cats

import cats.data.ValidatedNel
import cats.syntax.either._
import eu.timepit.refined.api.RefinedTypeOps

object syntax extends CatsRefinedTypeOpsSyntax

trait CatsRefinedTypeOpsSyntax {
  implicit class CatsRefinedTypeOps[FTP, T](rtOps: RefinedTypeOps[FTP, T]) {
    def validate(t: T): ValidatedNel[String, FTP] =
      rtOps.from(t).toValidatedNel
  }
}
