package eu.timepit.refined.jsonpath

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.jsonpath.string.JSONPath
import org.scalacheck.Prop._
import org.scalacheck.Properties

class StringValidateSpec extends Properties("JSONPathStringValidate") {

  property("JSONPath is valid") = secure {
    isValid[JSONPath]("$") && isValid[JSONPath]("@")
  }

  property("Illegal character at position 1 expected '.' or '[") = secure {
    showResult[JSONPath]("$X") ?=
      "JSONPath predicate failed: Illegal character at position 1 expected '.' or '["
  }

  property("Path must not end with a '.' or '..'") = secure {
    showResult[JSONPath]("$.") ?= "JSONPath predicate failed: Path must not end with a '.' or '..'"
  }
}
