package eu.timepit

import eu.timepit.refined.internal._

package object refined {
  val W = shapeless.Witness

  def refine[P]: Refine[P] = new Refine[P]
  def refineLit[P]: RefineLit[P] = new RefineLit[P]
  def refineM[P]: RefineM[P] = new RefineM[P]
}
