package eu.timepit.refined
package util

import eu.timepit.refined.string._
import shapeless.tag.@@

object string {

  /**
   * Creates a `scala.util.matching.Regex` from a validated string.
   *
   * Example: {{{
   * scala> import eu.timepit.refined.implicits._
   *      | import eu.timepit.refined.util.string.regex
   *
   * scala> regex(".*")
   * res1: scala.util.matching.Regex = .*
   *
   * scala> regex("{")
   * <console>:47: error: Regex predicate failed: Illegal repetition
   * {
   *        regex("{")
   *              ^
   * }}}
   */
  def regex(s: String @@ Regex): scala.util.matching.Regex =
    new scala.util.matching.Regex(s)

  /** Creates a `java.net.URI` from a validated string. */
  def uri(s: String @@ Uri): java.net.URI =
    new java.net.URI(s)

  /** Creates a `java.net.URL` from a validated string. */
  def url(s: String @@ Url): java.net.URL =
    new java.net.URL(s)

  /** Creates a `java.net.UUID` from a validated string. */
  def uuid(s: String @@ Uuid): java.util.UUID =
    java.util.UUID.fromString(s)

  /** Creates a `scala.xml.Elem` from a validated string. */
  def xml(s: String @@ Xml): scala.xml.Elem =
    scala.xml.XML.loadString(s)

  /** Creates a `javax.xml.xpath.XPathExpression` from a validated string. */
  def xpath(s: String @@ XPath): javax.xml.xpath.XPathExpression =
    javax.xml.xpath.XPathFactory.newInstance().newXPath().compile(s)
}
