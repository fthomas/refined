package eu.timepit.refined.types

import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.numeric.Interval

/** Module for date and time related refined types. */
object time {

  /** An `Int` in the range from 1 to 12 representing the month-of-year. */
  type Month = Int Refined Interval.Closed[1, 12]

  object Month extends RefinedTypeOps[Month, Int]

  /**
   * An `Int` in the range from 1 to 31 representing the day-of-month.
   * Note that the days from 29 to 31 are not valid for all months.
   */
  type Day = Int Refined Interval.Closed[1, 31]

  object Day extends RefinedTypeOps[Day, Int]

  /** An `Int` in the range from 0 to 23 representing the hour-of-day. */
  type Hour = Int Refined Interval.Closed[0, 23]

  object Hour extends RefinedTypeOps[Hour, Int]

  /** An `Int` in the range from 0 to 59 representing the minute-of-hour. */
  type Minute = Int Refined Interval.Closed[0, 59]

  object Minute extends RefinedTypeOps[Minute, Int]

  /** An `Int` in the range from 0 to 59 representing the second-of-minute. */
  type Second = Int Refined Interval.Closed[0, 59]

  object Second extends RefinedTypeOps[Second, Int]

  /** An `Int` in the range from 0 to 999 representing the millisecond-of-second. */
  type Millis = Int Refined Interval.Closed[0, 999]

  object Millis extends RefinedTypeOps[Millis, Int]
}

trait TimeTypes {
  final type Month = time.Month
  final val Month = time.Month

  final type Day = time.Day
  final val Day = time.Day

  final type Hour = time.Hour
  final val Hour = time.Hour

  final type Minute = time.Minute
  final val Minute = time.Minute

  final type Second = time.Second
  final val Second = time.Second

  final type Millis = time.Millis
  final val Millis = time.Millis
}
