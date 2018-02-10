package eu.timepit.refined.jsonpath

import com.jayway.jsonpath.internal.path.PathCompiler
import eu.timepit.refined.api.Validate

object string {

  /** Predicate that checks if a `String` is a valid JSON path. */
  final case class JSONPath()

  object JSONPath {
    implicit def jsonPathValidate: Validate.Plain[String, JSONPath] =
      Validate.fromPartial(PathCompiler.compile(_), "JSONPath", JSONPath())
  }
}
