package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.{RefType, Refined}
import eu.timepit.refined.collection.{NonEmpty, Size}
import eu.timepit.refined.string._
import eu.timepit.refined.types.string.TrimmedString
import org.scalacheck.Arbitrary
import shapeless.Witness

/**
 * Module that provides `Arbitrary` instances for `String` related
 * predicates.
 */
object string extends StringInstances with StringInstancesBinCompat1

trait StringInstances {

  implicit def endsWithArbitrary[F[_, _], S <: String](implicit
      rt: RefType[F],
      ws: Witness.Aux[S]
  ): Arbitrary[F[String, EndsWith[S]]] =
    arbitraryRefType(Arbitrary.arbString.arbitrary.map(_ + ws.value))

  implicit def startsWithArbitrary[F[_, _], S <: String](implicit
      rt: RefType[F],
      ws: Witness.Aux[S]
  ): Arbitrary[F[String, StartsWith[S]]] =
    arbitraryRefType(Arbitrary.arbString.arbitrary.map(ws.value + _))

  implicit def nonEmptyStringArbitrary[F[_, _]](implicit
      rt: RefType[F]
  ): Arbitrary[F[String, NonEmpty]] =
    collection.buildableNonEmptyArbitrary[F, String, Char]

  implicit def stringSizeArbitrary[F[_, _]: RefType, P](implicit
      arbChar: Arbitrary[Char],
      arbSize: Arbitrary[Int Refined P]
  ): Arbitrary[F[String, Size[P]]] =
    collection.buildableSizeArbitrary[F, String, Char, P]

  implicit def uuidStringArbitrary[F[_, _]](implicit
      rt: RefType[F]
  ): Arbitrary[F[String, Uuid]] =
    arbitraryRefType(Arbitrary.arbUuid.arbitrary.map(_.toString))

  implicit def validByteStringArbitrary[F[_, _]](implicit
      rt: RefType[F]
  ): Arbitrary[F[String, ValidByte]] =
    arbitraryRefType(Arbitrary.arbByte.arbitrary.map(_.toString))

  implicit def validShortStringArbitrary[F[_, _]](implicit
      rt: RefType[F]
  ): Arbitrary[F[String, ValidShort]] =
    arbitraryRefType(Arbitrary.arbShort.arbitrary.map(_.toString))

  implicit def validIntStringArbitrary[F[_, _]](implicit
      rt: RefType[F]
  ): Arbitrary[F[String, ValidInt]] =
    arbitraryRefType(Arbitrary.arbInt.arbitrary.map(_.toString))

  implicit def validLongStringArbitrary[F[_, _]](implicit
      rt: RefType[F]
  ): Arbitrary[F[String, ValidLong]] =
    arbitraryRefType(Arbitrary.arbLong.arbitrary.map(_.toString))

  implicit def validFloatStringArbitrary[F[_, _]](implicit
      rt: RefType[F]
  ): Arbitrary[F[String, ValidFloat]] =
    arbitraryRefType(Arbitrary.arbFloat.arbitrary.map(_.toString))

  implicit def validDoubleStringArbitrary[F[_, _]](implicit
      rt: RefType[F]
  ): Arbitrary[F[String, ValidDouble]] =
    arbitraryRefType(Arbitrary.arbDouble.arbitrary.map(_.toString))

  implicit def validBigIntStringArbitrary[F[_, _]](implicit
      rt: RefType[F]
  ): Arbitrary[F[String, ValidBigInt]] =
    arbitraryRefType(Arbitrary.arbBigInt.arbitrary.map(_.toString))

  implicit def validBigDecimalStringArbitrary[F[_, _]](implicit
      rt: RefType[F]
  ): Arbitrary[F[String, ValidBigDecimal]] =
    arbitraryRefType(Arbitrary.arbBigDecimal.arbitrary.map(_.toString))

}

trait StringInstancesBinCompat1 {

  implicit def trimmedStringArbitrary[F[_, _]](implicit
      rt: RefType[F]
  ): Arbitrary[F[String, Trimmed]] =
    arbitraryRefType(Arbitrary.arbString.arbitrary.map(TrimmedString.trim(_).value))

}
