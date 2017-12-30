package eu.timepit.refined.jsonpath

import eu.timepit.refined.api.Validate

object string extends StringValidate {

  /** Predicate that checks if a `String` is a valid JSON path. */
  final case class JSONPath()
}

private[refined] trait StringValidate {
  import string._

  implicit def jsonPathValidate: Validate.Plain[String, JSONPath] = {
    import com.jayway.jsonpath.internal.path.PathCompiler.compile

    def compileWithoutPredicates(str: String) = compile(str)

    Validate
      .fromPartial(compileWithoutPredicates, "JSONPath", JSONPath())
  }
}
