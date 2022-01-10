package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.api.Validate
import eu.timepit.refined.string._
import org.scalacheck.{Arbitrary, Properties}
import org.scalacheck.Prop._
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
  property("IPv6.isValid.full") = secure {
    isValid[IPv6]("2001:0db8:85a3:0000:0000:8a2e:0370:7334")
  }

  property("IPv6.isValid.noLeadingZeros") = secure {
    isValid[IPv6]("2001:db8:85a3:0:0:8a2e:370:7334")
  }

  property("IPv6.isValid.compact") = secure {
    isValid[IPv6]("2001:db8:85a3::8a2e:370:7334")
  }

  property("IPv6.isValid.local") = secure {
    isValid[IPv6]("::1")
  }

  property("IPv6.isValid.linkLocal") = secure {
    isValid[IPv6]("fe80::7:8%eth0")
  }

  property("IPv6.isValid.mapped") = secure {
    isValid[IPv6]("::ffff:255.255.255.255")
  }

  property("IPv6.isValid.embedded") = secure {
    isValid[IPv6]("2001:db8:122:344::192.0.2.33")
  }

  property("IPv6.showResult.Failed.Random") = secure {
    showResult[IPv6]("foo") ?= "Predicate failed: foo is a valid IPv6."
  }

  property("IPv6.showResult.Failed.DoubleCompact") = secure {
    showResult[IPv6]("2001::0::1234") ?= "Predicate failed: 2001::0::1234 is a valid IPv6."
  }

  private def validType[N: Arbitrary, P](name: String, invalidValue: String)(implicit
      v: Validate[String, P]
  ) = {
    property(name) = secure {
      forAll { (n: N) =>
        isValid[P](n.toString) &&
        (showResult[P](n.toString) ?= s"$name predicate passed.")
      }
    }
    property(s"$name.showResult.Failed") = secure {
      showResult[P](invalidValue).startsWith(s"$name predicate failed")
    }
  }

  validType[Byte, ValidByte]("ValidByte", Short.MaxValue.toString)
  validType[Short, ValidShort]("ValidShort", Int.MaxValue.toString)
  validType[Int, ValidInt]("ValidInt", Long.MaxValue.toString)
  validType[Long, ValidLong]("ValidLong", "1.0")
  validType[Float, ValidFloat]("ValidFloat", "a")
  validType[Double, ValidDouble]("ValidDouble", "a")
  validType[BigInt, ValidBigInt]("ValidBigInt", "1.0")
  validType[BigDecimal, ValidBigDecimal]("ValidBigDecimal", "a")
  validType[Boolean, ValidBoolean]("ValidBoolean", "a")
}