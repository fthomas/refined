package eu.timepit.refined

import eu.timepit.refined.implicits._
import eu.timepit.refined.util.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.test.illTyped

class StringUtilSpecJvm extends Properties("util.string") {

  property("regex success") = secure {
    regex("(a|b)").toString ?= "(a|b)".r.toString
  }

  property("regex failure") = secure {
    illTyped("""regex("(a|b")""", "(?s)Regex predicate failed.*")
    true
  }

  property("url success") = secure {
    url("http://example.com").toString ?= new java.net.URL("http://example.com").toString
  }

  property("url failure") = secure {
    illTyped("""url("http//example.com")""", "(?s)Url predicate failed.*")
    true
  }

  property("xml success") = secure {
    xml("<root></root>") == scala.xml.XML.loadString("<root></root>")
  }

  property("xml failure") = secure {
    illTyped("""xml("<root>")""", "(?s)Xml predicate failed.*")
    true
  }

  property("xpath success") = secure {
    xpath("A//B/*[1]")
    true
  }

  property("xpath failure") = secure {
    illTyped("""xpath("A//B/*[1")""", "(?s)XPath predicate failed.*")
    true
  }
}
