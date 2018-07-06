package eu.timepit.refined.cats

import cats.data.{NonEmptyList, ValidatedNel}
import cats.syntax.either._
import eu.timepit.refined.api.RefinedTypeOps
import eu.timepit.refined.types.numeric.PosInt

object syntax extends CatsRefinedTypeOpsSyntax with CatsNonEmptyListSyntax

trait CatsRefinedTypeOpsSyntax {
  implicit class CatsRefinedTypeOps[FTP, T](rtOps: RefinedTypeOps[FTP, T]) {
    def validate(t: T): ValidatedNel[String, FTP] =
      rtOps.from(t).toValidatedNel
  }
}

trait CatsNonEmptyListSyntax {
  implicit class CatsNonEmptyListRefinedOps[A](nel: NonEmptyList[A]) {
    def refinedSize: PosInt = PosInt.unsafeFrom(nel.size)
  }
}
