package eu.timepit.refined

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

  property("Url.validate success") = secure {
    Predicate[Url, String].validate("http://example.com") ?= None
  }

  property("Url.validate failure") = secure {
    Predicate[Url, String].validate("htp://example.com") ?=
      Some("Predicate isValidUrl(\"htp://example.com\") failed: unknown protocol: htp")
  }

  property("XPath.validate success") = secure {
    Predicate[XPath, String].validate("A//B/*[1]") ?= None
  }

  property("XPath.validate failure") = secure {
    Predicate[XPath, String].validate("A//B/*[1") ?=
      Some("Predicate isValidXPath(\"A//B/*[1\") failed: " +
        "javax.xml.transform.TransformerException: Expected ], but found: ")
  }
}
