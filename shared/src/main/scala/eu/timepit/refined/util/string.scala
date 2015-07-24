package eu.timepit.refined
package util

import java.net.{ URI, URL }
import java.util.UUID

import eu.timepit.refined.string._
import shapeless.tag._

object string {

  /**
   * Creates a `scala.util.matching.Regex` from a validated string.
   *
   * @example {{{
   * scala> import eu.timepit.refined.implicits._
   *      | import eu.timepit.refined.util.string.regex
   *
   * scala> regex(".*")
   * res1: scala.util.matching.Regex = .*
   *
   * scala> regex("{")
   * <console>:41: error: Predicate isValidRegex("{") failed: Illegal repetition
   * {
   *        regex("{")
   *              ^
   * }}}
   */
  def regex(s: String @@ Regex): scala.util.matching.Regex = s.r

  /** Creates a `java.net.URI` from a validated string. */
  def uri(s: String @@ Uri): URI = new URI(s)

  /** Creates a `java.net.URL` from a validated string. */
  def url(s: String @@ Url): URL = new URL(s)

  /** Creates a `java.net.UUID` from a validated string. */
  def uuid(s: String @@ Uuid): UUID = UUID.fromString(s)
}
