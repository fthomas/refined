package eu.timepit.refined.scalaxml

import eu.timepit.refined.api.Refined
import eu.timepit.refined.scalaxml.string.Xml

object util {
  /** Creates a `scala.xml.Elem` from a validated string. */
  def xml(s: String Refined Xml): scala.xml.Elem =
    scala.xml.XML.loadString(s.value)
}
