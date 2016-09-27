package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.scalacheck.generic._
import org.scalacheck.Properties

class GenericArbitrarySpec extends Properties("GenericArbitrary") {

  property("Equal") = checkArbitraryRefType[Refined, Int, Equal[W.`100`.T]]
}
