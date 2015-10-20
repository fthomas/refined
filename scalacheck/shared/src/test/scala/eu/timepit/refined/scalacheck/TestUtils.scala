package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.{ Refined, Validate }
import org.scalacheck.Prop._
import org.scalacheck.{ Arbitrary, Prop }

object TestUtils {

  def checkArbitrary[T, P](implicit arb: Arbitrary[Refined[T, P]], v: Validate[T, P]): Prop =
    forAll((tp: Refined[T, P]) => v.isValid(tp.get))
}
