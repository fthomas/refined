package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.scalacheck.any._
import org.scalacheck.Properties

class AnyArbitrarySpec extends Properties("AnyArbitrary") {

  property("NonEmpty") = checkArbitraryRefType[Refined, List[Int], NonEmpty]
}
