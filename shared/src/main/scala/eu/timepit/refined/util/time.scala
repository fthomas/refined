package eu.timepit.refined
package util

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Interval

object time {

  /** An `Int` in the range from 1 to 12. */
  type Month = Int Refined Interval[W.`1`.T, W.`12`.T]

  /** An `Int` in the range from 1 to 31. */
  type DayOfMonth = Int Refined Interval[W.`1`.T, W.`31`.T]

  /** An `Int` in the range from 0 to 23. */
  type Hour = Int Refined Interval[W.`0`.T, W.`23`.T]

  /** An `Int` in the range from 0 to 59. */
  type Minute = Int Refined Interval[W.`0`.T, W.`59`.T]

  /** An `Int` in the range from 0 to 59. */
  type Second = Int Refined Interval[W.`0`.T, W.`59`.T]

  // Add safe constructors for java.time types once we require Java 8 or higher.
}
