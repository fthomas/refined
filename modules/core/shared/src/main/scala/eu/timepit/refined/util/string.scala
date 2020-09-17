package eu.timepit.refined.util

import eu.timepit.refined.api.Refined
import eu.timepit.refined.string._

/**
 * Module for statically checking constructors of types that can be
 * instantiated with `String`s.
 *
 * @see [[https://github.com/fthomas/refined/blob/master/docs/util_string.md]]
 */
object string {

  /** Creates a `[[scala.util.matching.Regex]]` from a validated string. */
  def regex(s: String Refined Regex): scala.util.matching.Regex =
    new scala.util.matching.Regex(s.value)

  /** Creates a `java.net.URI` from a validated string. */
  def uri(s: String Refined Uri): java.net.URI =
    new java.net.URI(s.value)

  /** Creates a `java.net.URL` from a validated string. */
  def url(s: String Refined Url): java.net.URL =
    new java.net.URL(s.value)

  /** Creates a `java.net.UUID` from a validated string. */
  def uuid(s: String Refined Uuid): java.util.UUID =
    java.util.UUID.fromString(s.value)

  /** Creates a `scala.xml.Elem` from a validated string. */
  def xml(s: String Refined Xml): scala.xml.Elem =
    scala.xml.XML.loadString(s.value)

  /** Creates a `javax.xml.xpath.XPathExpression` from a validated string. */
  def xpath(s: String Refined XPath): javax.xml.xpath.XPathExpression =
    javax.xml.xpath.XPathFactory.newInstance().newXPath().compile(s.value)
}
