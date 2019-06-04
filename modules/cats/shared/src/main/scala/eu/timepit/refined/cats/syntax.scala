package eu.timepit.refined.cats

import cats.data.{NonEmptyList, ValidatedNel, ValidatedNec}
import cats.syntax.either._
import eu.timepit.refined.api.RefinedTypeOps
import eu.timepit.refined.types.numeric.PosInt

object syntax extends CatsRefinedTypeOpsSyntax with CatsNonEmptyListSyntax

trait CatsRefinedTypeOpsSyntax {
  implicit class CatsRefinedTypeOps[FTP, T](rtOps: RefinedTypeOps[FTP, T]) {
    def validateNel(t: T): ValidatedNel[String, FTP] =
      rtOps.from(t).toValidatedNel
    
    def validateNec(t: T): ValidatedNec[String, FTP] =
      rtOps.from(t).toValidatedNec
  }
}

trait CatsNonEmptyListSyntax {
  implicit class CatsNonEmptyListRefinedOps[A](nel: NonEmptyList[A]) {
    def refinedSize: PosInt = PosInt.unsafeFrom(nel.size)
  }
}
