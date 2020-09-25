package eu.timepit.refined.scalacheck

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.scalacheck.any._
import eu.timepit.refined.string.MatchesRegex
import org.scalacheck.Properties

class AnyArbitrarySpec extends Properties("AnyArbitrary") {

  property("MatchesRegex[S]") =
    checkArbitraryRefinedType[String Refined MatchesRegex[W.`".{2,}"`.T]]

  property("NonEmpty") = checkArbitraryRefinedType[List[Int] Refined NonEmpty]
}
