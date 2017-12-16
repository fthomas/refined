package eu.timepit.refined.types

import eu.timepit.refined.W
import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.numeric.Interval

/** Module for date and time related refined types. */
object time extends TimeTypes

trait TimeTypes {

  /** An `Int` in the range from 1 to 12 representing the month-of-year. */
  type Month = Int Refined Interval.Closed[W.`1`.T, W.`12`.T]

  object Month extends RefinedTypeOps[Month, Int]

  /**
   * An `Int` in the range from 1 to 31 representing the day-of-month.
   * Note that the days from 29 to 31 are not valid for all months.
   */
  type Day = Int Refined Interval.Closed[W.`1`.T, W.`31`.T]

  /** An `Int` in the range from 0 to 23 representing the hour-of-day. */
  type Hour = Int Refined Interval.Closed[W.`0`.T, W.`23`.T]

  object Hour extends RefinedTypeOps[Hour, Int]

  /** An `Int` in the range from 0 to 59 representing the minute-of-hour. */
  type Minute = Int Refined Interval.Closed[W.`0`.T, W.`59`.T]

  /** An `Int` in the range from 0 to 59 representing the second-of-minute. */
  type Second = Int Refined Interval.Closed[W.`0`.T, W.`59`.T]

  object Second extends RefinedTypeOps[Second, Int]

  /** An `Int` in the range from 0 to 999 representing the millisecond-of-second. */
  type Millis = Int Refined Interval.Closed[W.`0`.T, W.`999`.T]
}
