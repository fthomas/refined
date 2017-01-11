package eu.timepit.refined.types

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Interval

/** Module for refined types that are related to the Internet protocol suite. */
object net extends NetTypes

trait NetTypes {

  /** An `Int` in the range from 0 to 65535 representing a port number. */
  type PortNumber = Int Refined Interval.Closed[W.`0`.T, W.`65535`.T]
}
