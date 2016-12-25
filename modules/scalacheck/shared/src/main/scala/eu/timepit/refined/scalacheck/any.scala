package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.{RefType, Validate}
import org.scalacheck.Arbitrary

/**
 * Module that provides an `Arbitrary` instance for any refined type
 * `F[T, P]`.
 *
 * This instance uses the `Arbitrary` instance of the base type `T` and
 * the corresponding `Validate[T, P]` instance of the refinement to filter
 * out invalid values. For most refinements this will cause ScalaCheck to
 * fail because this instance will discard too many values.
 */
object any {

  implicit def arbitraryFromValidate[F[_, _], T, P](
      implicit rt: RefType[F],
      v: Validate[T, P],
      arb: Arbitrary[T]
  ): Arbitrary[F[T, P]] =
    arbitraryRefType(arb.arbitrary.filter(v.isValid))
}
