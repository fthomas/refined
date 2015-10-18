package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.scalacheck.anyArbitrary._
import eu.timepit.refined.scalacheck.stringArbitrary._
import eu.timepit.refined.scalacheck.TestUtils._
import eu.timepit.refined.string.{ EndsWith, MatchesRegex, StartsWith }
import org.scalacheck.Properties

class StringArbitrarySpec extends Properties("StringArbitrary") {

  property("EndsWith") = checkArbitrary[Refined, String, EndsWith[W.`"abc"`.T]]

  property("MatchesRegex") = checkArbitrary[Refined, String, MatchesRegex[W.`".{2,}"`.T]]

  property("StartsWith") = checkArbitrary[Refined, String, StartsWith[W.`"abc"`.T]]
}
