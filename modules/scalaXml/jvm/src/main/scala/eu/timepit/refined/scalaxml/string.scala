package eu.timepit.refined.scalaxml

import eu.timepit.refined.api.Validate

object string {

  /** Predicate that checks if a `String` is well-formed XML. */
  final case class Xml()

  object Xml {
    implicit def xmlValidate: Validate.Plain[String, Xml] =
      Validate.fromPartial(scala.xml.XML.loadString, "Xml", Xml())
  }
}
