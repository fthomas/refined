package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.scalacheck.any._
import eu.timepit.refined.scalacheck.string._
import eu.timepit.refined.string._
import org.scalacheck.Properties

class StringArbitrarySpec extends Properties("StringArbitrary") {

  property("EndsWith") = checkArbitraryRefType[
      Refined, String, EndsWith[W.`"abc"`.T]]

  property("MatchesRegex") = checkArbitraryRefType[
      Refined, String, MatchesRegex[W.`".{2,}"`.T]]

  property("StartsWith") = checkArbitraryRefType[
      Refined, String, StartsWith[W.`"abc"`.T]]

  // collection predicates

  property("NonEmpty") = checkArbitraryRefType[Refined, String, NonEmpty]
}
