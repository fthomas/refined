package eu.timepit.refined.scalacheck

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.types.all._
import org.scalacheck.{Cogen, Properties}

class PackageSpec extends Properties("Package") {

  property("Cogen[Short Refined Positive]") = wellTyped(Cogen[Short Refined Positive])

  property("Cogen[LowerCaseChar]") = wellTyped(Cogen[LowerCaseChar])

  property("Cogen[NonEmptyString]") = wellTyped(Cogen[NonEmptyString])

  property("Cogen[PosInt]") = wellTyped(Cogen[PosInt])
}
