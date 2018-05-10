package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.{Refined, RefType}
import eu.timepit.refined.collection.{NonEmpty, Size}
import eu.timepit.refined.string.{EndsWith, StartsWith, Trimmed}
import eu.timepit.refined.types.string.TrimmedString
import org.scalacheck.Arbitrary
import shapeless.Witness

/**
 * Module that provides `Arbitrary` instances for `String` related
 * predicates.
 */
object string extends StringInstances

trait StringInstances {

  implicit def endsWithArbitrary[F[_, _], S <: String](
      implicit rt: RefType[F],
      ws: Witness.Aux[S]
  ): Arbitrary[F[String, EndsWith[S]]] =
    arbitraryRefType(Arbitrary.arbString.arbitrary.map(_ + ws.value))

  implicit def startsWithArbitrary[F[_, _], S <: String](
      implicit rt: RefType[F],
      ws: Witness.Aux[S]
  ): Arbitrary[F[String, StartsWith[S]]] =
    arbitraryRefType(Arbitrary.arbString.arbitrary.map(ws.value + _))

  implicit def nonEmptyStringArbitrary[F[_, _]](
      implicit rt: RefType[F]
  ): Arbitrary[F[String, NonEmpty]] =
    collection.buildableNonEmptyArbitrary[F, String, Char]

  implicit def trimmedStringArbitrary[F[_, _]](
      implicit rt: RefType[F]
  ): Arbitrary[F[String, Trimmed]] =
    arbitraryRefType(Arbitrary.arbString.arbitrary.map(TrimmedString.trim(_).value))

  implicit def stringSizeArbitrary[F[_, _]: RefType, P](
      implicit
      arbChar: Arbitrary[Char],
      arbSize: Arbitrary[Int Refined P]
  ): Arbitrary[F[String, Size[P]]] =
    collection.buildableSizeArbitrary[F, String, Char, P]
}
