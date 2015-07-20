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
    illTyped("""regex("(a|b")""", "(?s)Predicate isValidRegex.*failed.*")
    true
  }

  property("uri success") = secure {
    uri("file:///dev/null") ?= new java.net.URI("file:///dev/null")
  }

  property("uri failure") = secure {
    illTyped("""uri("file:// /dev/null")""", "(?s)Predicate isValidUri.*failed.*")
    true
  }

  property("url success") = secure {
    url("http://example.com").toString ?= new java.net.URL("http://example.com").toString
  }

  property("url failure") = secure {
    illTyped("""url("http//example.com")""", "(?s)Predicate isValidUrl.*failed.*")
    true
  }
}
