package eu.timepit

import eu.timepit.refined.internal.{ Refine, RefineLit }

package object refined {
  def refine[P]: Refine[P] = new Refine[P]
  def refineLit[P]: RefineLit[P] = new RefineLit[P]

  val W = shapeless.Witness
}
