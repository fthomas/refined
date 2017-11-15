package eu.timepit.refined.types

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.macros.refinedCompanion
import eu.timepit.refined.numeric.Interval

/** Module for refined types that are related to the Internet protocol suite. */
object net extends NetTypes

trait NetTypes {

  /** An `Int` in the range from 0 to 65535 representing a port number. */
  @refinedCompanion
  type PortNumber = Int Refined Interval.Closed[W.`0`.T, W.`65535`.T]

  /** An `Int` in the range from 0 to 1023 representing a port number. */
  @refinedCompanion
  type SystemPortNumber = Int Refined Interval.Closed[W.`0`.T, W.`1023`.T]

  /** An `Int` in the range from 1024 to 49151 representing a port number. */
  @refinedCompanion
  type UserPortNumber = Int Refined Interval.Closed[W.`1024`.T, W.`49151`.T]

  /** An `Int` in the range from 49152 to 65535 representing a port number. */
  @refinedCompanion
  type DynamicPortNumber = Int Refined Interval.Closed[W.`49152`.T, W.`65535`.T]

  /** An `Int` in the range from 1024 to 65535 representing a port number. */
  @refinedCompanion
  type NonSystemPortNumber = Int Refined Interval.Closed[W.`1024`.T, W.`65535`.T]
}
