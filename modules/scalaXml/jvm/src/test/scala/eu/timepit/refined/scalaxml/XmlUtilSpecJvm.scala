package eu.timepit.refined.scalaxml

import eu.timepit.refined.auto._
import eu.timepit.refined.scalaxml.util._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.test.illTyped

class XmlUtilSpecJvm extends Properties("scalaxml.util.string") {

  property("xml success") = secure {
    xml("<root></root>") == scala.xml.XML.loadString("<root></root>")
  }

  property("xml failure") = secure {
    illTyped("""xml("<root>")""", "Xml predicate failed.*")
    true
  }
}
