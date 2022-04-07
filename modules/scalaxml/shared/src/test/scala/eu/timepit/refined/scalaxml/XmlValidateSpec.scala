package eu.timepit.refined.scalaxml

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.scalaxml.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class XmlValidateSpec extends Properties("XmlValidate") {
  property("Xml.isValid") = secure {
    isValid[Xml]("<root></root>")
  }

  property("Xml.showResult") = secure {
    showResult[Xml]("<root>") ?=
      "Xml predicate failed: XML document structures must start and end within the same entity."
  }
}
