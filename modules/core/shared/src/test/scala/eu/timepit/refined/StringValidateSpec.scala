package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.Witness

class StringValidateSpec extends Properties("StringValidate") {

  property("EndsWith.isValid") = secure {
    val s = "abcd"
    val suffix = Witness("cd")
    isValid[EndsWith[suffix.T]](s) ?= s.endsWith(suffix.value)
  }

  property("EndsWith.showExpr") = secure {
    showExpr[EndsWith[W.`"cd"`.T]]("abcd") ?= """"abcd".endsWith("cd")"""
  }

  property("MatchesRegex.isValid") = forAll { (s: String) =>
    isValid[MatchesRegex[W.`".{2,10}"`.T]](s) ?= s.matches(".{2,10}")
  }

  property("MatchesRegex.showExpr") = secure {
    showExpr[MatchesRegex[W.`".{2,10}"`.T]]("Hello") ?= """"Hello".matches(".{2,10}")"""
  }

  property("Regex.isValid") = secure {
    isValid[Regex](".*")
  }

  property("Regex.showExpr") = secure {
    showExpr[Regex]("(a|b)") ?= """isValidRegex("(a|b)")"""
  }

  property("StartsWith.isValid") = secure {
    val s = "abcd"
    val prefix = Witness("ab")
    isValid[StartsWith[prefix.T]](s) ?= s.startsWith(prefix.value)
  }

  property("StartsWith.showExpr") = secure {
    showExpr[StartsWith[W.`"ab"`.T]]("abcd") ?= """"abcd".startsWith("ab")"""
  }

  property("Uri.isValid") = secure {
    isValid[Uri]("/a/b/c")
  }

  property("Uri.showResult") = secure {
    val jvmErr = showResult[Uri](" /a/b/c") ?=
      "Uri predicate failed: Illegal character in path at index 0:  /a/b/c"

    val jsErr = showResult[Uri](" /a/b/c") ?=
      "Uri predicate failed: Malformed URI in  /a/b/c at -1"

    jvmErr || jsErr
  }

  property("Uuid.isValid") = secure {
    isValid[Uuid]("9ecce884-47fe-4ba4-a1bb-1a3d71ed6530")
  }

  property("Uuid.showResult.Passed") = secure {
    showResult[Uuid]("9ecce884-47fe-4ba4-a1bb-1a3d71ed6530") ?= "Uuid predicate passed."
  }

  property("Uuid.showResult.Failed") = secure {
    showResult[Uuid]("whops") ?= "Uuid predicate failed: Invalid UUID string: whops"
  }

  property("IPv4.isValid") = secure {
    isValid[IPv4]("10.0.0.1")
  }

  property("IPv4.showResult.InvalidOctet") = secure {
    showResult[IPv4]("10.0.256.1") ?= "Predicate failed: 10.0.256.1 is a valid IPv4."
  }

  property("IPv4.showResult.Failed") = secure {
    showResult[IPv4]("::1") ?= "Predicate failed: ::1 is a valid IPv4."
  }
}
