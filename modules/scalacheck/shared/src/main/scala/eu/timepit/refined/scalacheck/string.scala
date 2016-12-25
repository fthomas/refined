package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.string.{EndsWith, StartsWith}
import org.scalacheck.Arbitrary
import shapeless.Witness

/**
 * Module that provides `Arbitrary` instances for `String` related
 * predicates.
 */
object string {

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
}
