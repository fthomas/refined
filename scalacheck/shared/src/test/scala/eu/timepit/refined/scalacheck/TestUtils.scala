package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.{ RefType, Validate }
import org.scalacheck.Arbitrary
import org.scalacheck.Prop._

object TestUtils {

  def checkArbitrary[F[_, _], T, P](implicit arb: Arbitrary[F[T, P]], rt: RefType[F], v: Validate[T, P]) =
    forAll((tp: F[T, P]) => v.isValid(rt.unwrap(tp)))
}
