package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.string.{ EndsWith, StartsWith }
import org.scalacheck.Arbitrary
import shapeless.Witness

object stringArbitrary {

  implicit def endsWithArbitrary[F[_, _], S <: String](
    implicit
    rt: RefType[F],
    ws: Witness.Aux[S]
  ): Arbitrary[F[String, EndsWith[S]]] =
    Arbitrary(Arbitrary.arbString.arbitrary.map(_ + ws.value).map(rt.unsafeWrap))

  implicit def startsWithArbitrary[F[_, _], S <: String](
    implicit
    rt: RefType[F],
    ws: Witness.Aux[S]
  ): Arbitrary[F[String, StartsWith[S]]] =
    Arbitrary(Arbitrary.arbString.arbitrary.map(ws.value + _).map(rt.unsafeWrap))
}
