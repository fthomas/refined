package eu.timepit.refined

import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class StringPredicateSpecJvm extends Properties("StringPredicate") {

  property("Regex.validate failure") = secure {
    Predicate[Regex, String].validate("(a|b") ?=
      Some(
        """Predicate isValidRegex("(a|b") failed: Unclosed group near index 4
          |(a|b
          |    ^""".stripMargin
      )
  }

  property("Uri.validate success") = secure {
    Predicate[Uri, String].validate("/a/b/c") ?= None
  }

  property("Uri.validate failure") = secure {
    Predicate[Uri, String].validate(" /a/b/c") ?=
      Some("Predicate isValidUri(\" /a/b/c\") failed: Illegal character in path at index 0:  /a/b/c")
  }

  property("Url.validate success") = secure {
    Predicate[Url, String].validate("http://example.com") ?= None
  }

  property("Url.validate failure") = secure {
    Predicate[Url, String].validate("htp://example.com") ?=
      Some("Predicate isValidUrl(\"htp://example.com\") failed: unknown protocol: htp")
  }
}
