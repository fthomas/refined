package eu.timepit.refined.cats

import cats.data.{NonEmptyList, NonEmptyVector, ValidatedNec, ValidatedNel}
import cats.syntax.either._
import eu.timepit.refined.api.RefinedTypeOps
import eu.timepit.refined.types.numeric.PosInt

object syntax extends CatsRefinedTypeOpsSyntax with CatsNonEmptyListSyntax with CatsNonEmptyVectorSyntax

trait CatsRefinedTypeOpsSyntax {
  implicit class CatsRefinedTypeOps[FTP, T](rtOps: RefinedTypeOps[FTP, T]) {
    def validate(t: T): ValidatedNel[String, FTP] =
      validateNel(t)

    def validateNec(t: T): ValidatedNec[String, FTP] =
      rtOps.from(t).toValidatedNec

    def validateNel(t: T): ValidatedNel[String, FTP] =
      rtOps.from(t).toValidatedNel
  }
}

trait CatsNonEmptyListSyntax {
  implicit class CatsNonEmptyListRefinedOps[A](nel: NonEmptyList[A]) {
    def refinedSize: PosInt = PosInt.unsafeFrom(nel.size)

    def take(n: PosInt): NonEmptyList[A] =
      NonEmptyList(nel.head, nel.tail.take(n.value - 1))
  }
}

trait CatsNonEmptyVectorSyntax {
  implicit class CatsNonEmptyVectorRefinedOps[A](nev: NonEmptyVector[A]) {
    def take(n: PosInt): NonEmptyVector[A] =
      NonEmptyVector.fromVectorUnsafe(nev.toVector.take(n.value))
  }
}
