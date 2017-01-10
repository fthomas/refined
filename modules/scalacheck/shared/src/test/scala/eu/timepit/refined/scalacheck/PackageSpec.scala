package eu.timepit.refined
package scalacheck

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.types.all._
import org.scalacheck.{Cogen, Properties}

class PackageSpec extends Properties("Package") {

  property("Cogen instances") = wellTyped {
    val x0 = Cogen[LowerCaseChar]
    val x1 = Cogen[NonEmptyString]
    val x2 = Cogen[PosInt]
  }
}
