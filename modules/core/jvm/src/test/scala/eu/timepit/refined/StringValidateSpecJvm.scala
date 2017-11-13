package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class StringValidateSpecJvm extends Properties("StringValidate") {
  property("IPv4.isValid") = secure {
    isValid[IPv4]("10.0.0.1")
  }

  property("IPv4.showResult.Failed") = secure {
    showResult[IPv4]("::1") ?= "IPv4 predicate failed: requirement failed"
  }

  property("IPv6.isValid") = secure {
    isValid[IPv6]("::1")
  }

  property("IPv6.showResult.Failed") = secure {
    showResult[IPv6]("10.0.0.1") ?= "IPv6 predicate failed: requirement failed"
  }

  property("Regex.showResult") = secure {
    showResult[Regex]("(a|b") ?=
      """Regex predicate failed: Unclosed group near index 4
        |(a|b
        |    ^""".stripMargin
  }

  property("Url.isValid") = secure {
    isValid[Url]("http://example.com")
  }

  property("Url.showResult") = secure {
    showResult[Url]("htp://example.com") ?= "Url predicate failed: unknown protocol: htp"
  }

  property("Xml.isValid") = secure {
    isValid[Xml]("<root></root>")
  }

  property("Xml.showResult") = secure {
    showResult[Xml]("<root>") ?=
      "Xml predicate failed: XML document structures must start and end within the same entity."
  }

  property("XPath.isValid") = secure {
    isValid[XPath]("A//B/*[1]")
  }

  property("XPath.showResult") = secure {
    showResult[XPath]("A//B/*[1") ?=
      "XPath predicate failed: javax.xml.transform.TransformerException: Expected ], but found: "
  }
}
