package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.{ RefType, Validate }
import org.scalacheck.Arbitrary

object any {

  implicit def arbitraryFromValidate[F[_, _], T, P](
    implicit
    rt: RefType[F],
    v: Validate[T, P],
    arb: Arbitrary[T]
  ): Arbitrary[F[T, P]] =
    arbitraryRefType(arb.arbitrary.filter(v.isValid))
}
