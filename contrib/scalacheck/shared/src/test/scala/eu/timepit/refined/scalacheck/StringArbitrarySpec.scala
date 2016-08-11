package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.scalacheck.any._
import eu.timepit.refined.scalacheck.string._
import eu.timepit.refined.string._
import org.scalacheck.Properties

class StringArbitrarySpec extends Properties("StringArbitrary") {

  property("EndsWith") =
    checkArbitraryRefinedType[String Refined EndsWith[W.`"abc"`.T]]

  property("MatchesRegex") =
    checkArbitraryRefinedType[String Refined MatchesRegex[W.`".{2,}"`.T]]

  property("StartsWith") =
    checkArbitraryRefinedType[String Refined StartsWith[W.`"abc"`.T]]

  // collection predicates

  property("NonEmpty") =
    checkArbitraryRefinedType[String Refined NonEmpty]
}
