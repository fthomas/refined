package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.types.all._
import org.scalacheck.{Cogen, Prop, Properties}

class PackageSpec extends Properties("Package") {
  // this is just copied from core since core’s test configuration is built for scalacheck 1.14 only
  def wellTyped[A](body: => A): Prop = Prop.secure {
    body
    true
  }

  property("Cogen[Short Refined Positive]") = wellTyped(Cogen[Short Refined Positive])

  property("Cogen[LowerCaseChar]") = wellTyped(Cogen[LowerCaseChar])

  property("Cogen[NonEmptyString]") = wellTyped(Cogen[NonEmptyString])

  property("Cogen[PosInt]") = wellTyped(Cogen[PosInt])
}
