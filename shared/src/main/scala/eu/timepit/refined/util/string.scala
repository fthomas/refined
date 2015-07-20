package eu.timepit.refined
package util

import java.net.{ URI, URL }

import eu.timepit.refined.string._
import shapeless.tag._

object string {

  /** Creates a `scala.util.matching.Regex` from a validated string. */
  def regex(s: String @@ Regex): scala.util.matching.Regex = s.r

  /** Creates a `java.net.URI` from a validated string. */
  def uri(s: String @@ Uri): URI = new URI(s)

  /** Creates a `java.net.URL` from a validated string. */
  def url(s: String @@ Url): URL = new URL(s)
}
