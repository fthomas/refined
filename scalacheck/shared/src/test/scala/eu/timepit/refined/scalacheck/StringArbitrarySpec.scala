package eu.timepit.refined
package scalacheck

import eu.timepit.refined.scalacheck.any._
import eu.timepit.refined.scalacheck.string._
import eu.timepit.refined.scalacheck.TestUtils._
import eu.timepit.refined.string._
import org.scalacheck.Properties

class StringArbitrarySpec extends Properties("StringArbitrary") {

  property("EndsWith") = checkArbitrary[String, EndsWith[W.`"abc"`.T]]

  property("MatchesRegex") = checkArbitrary[String, MatchesRegex[W.`".{2,}"`.T]]

  property("StartsWith") = checkArbitrary[String, StartsWith[W.`"abc"`.T]]
}
