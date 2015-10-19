package eu.timepit.refined
package scalacheck

import eu.timepit.refined.scalacheck.anyArbitrary._
import eu.timepit.refined.scalacheck.stringArbitrary._
import eu.timepit.refined.scalacheck.TestUtils._
import eu.timepit.refined.string.{ EndsWith, MatchesRegex, StartsWith }
import org.scalacheck.Properties

class StringArbitrarySpec extends Properties("StringArbitrary") {

  property("EndsWith") = checkArbitrary[String, EndsWith[W.`"abc"`.T]]

  property("MatchesRegex") = checkArbitrary[String, MatchesRegex[W.`".{2,}"`.T]]

  property("StartsWith") = checkArbitrary[String, StartsWith[W.`"abc"`.T]]
}
