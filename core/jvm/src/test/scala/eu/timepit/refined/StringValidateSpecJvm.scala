package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class StringValidateSpecJvm extends Properties("StringValidate") {

  property("Regex.showResult") = secure {
    showResult[Regex]("(a|b") ?= """Regex predicate failed: Unclosed group near index 4
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
    showResult[Xml]("<root>") ?= "Xml predicate failed: XML document structures must start and end within the same entity."
  }

  property("XPath.isValid") = secure {
    isValid[XPath]("A//B/*[1]")
  }

  property("XPath.showResult") = secure {
    showResult[XPath]("A//B/*[1") ?= "XPath predicate failed: javax.xml.transform.TransformerException: Expected ], but found: "
  }
}
