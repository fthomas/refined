package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.string.{EndsWith, StartsWith}
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
  ): Arbitrary[F[String, NonEmpty]] = {
    val nonEmptyStringGen = for {
      s <- Arbitrary.arbString.arbitrary
      c <- Arbitrary.arbChar.arbitrary
    } yield s + c
    arbitraryRefType(nonEmptyStringGen)
  }
}
