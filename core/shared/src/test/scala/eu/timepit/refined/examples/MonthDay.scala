package eu.timepit.refined
package examples

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Interval

object MonthDay extends App {

  sealed trait Month {
    type LastDay <: Int
  }

  object January extends Month {
    type LastDay = W.`31`.T
  }

  object February extends Month {
    // let's ignore leap years
    type LastDay = W.`28`.T
  }

  def printMonthDay(month: Month)(day: Int Refined Interval.Closed[W.`1`.T, month.LastDay]) =
    println(s"$month.$day")

  printMonthDay(January)(31)
  printMonthDay(February)(28)
  //printMonthDay(February)(31) // does not compile
  /*
    [error] Right predicate of (!(31 < 1) && !(31 > 28)) failed: Predicate (31 > 28) did not fail.
    [error]   printMonthDay(February)(31)
    [error]                           ^
  */
}
