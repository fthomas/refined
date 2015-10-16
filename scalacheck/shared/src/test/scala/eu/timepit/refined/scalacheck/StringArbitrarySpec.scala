package eu.timepit.refined
package scalacheck

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.scalacheck.stringArbitrary._
import eu.timepit.refined.string.{ EndsWith, StartsWith }
import org.scalacheck.Prop._
import org.scalacheck.Properties

class StringArbitrarySpec extends Properties("StringArbitrary") {

  property("EndsWith") = forAll { (s: String Refined EndsWith[W.`"abc"`.T]) =>
    s.endsWith("abc")
  }

  property("StartsWith") = forAll { (s: String Refined StartsWith[W.`"abc"`.T]) =>
    s.startsWith("abc")
  }
}
