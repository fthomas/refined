package eu.timepit

import eu.timepit.refined.internal._
import shapeless.tag.@@

package object refined {
  val W = shapeless.Witness

  def refineV[P]: Refine[P, Refined] = new Refine[P, Refined]
  def refineT[P]: Refine[P, @@] = new Refine[P, @@]

  def refineMV[P]: RefineM[P, Refined] = new RefineM[P, Refined]
  def refineMT[P]: RefineM[P, @@] = new RefineM[P, @@]

  @deprecated("refine is deprecated in favor of refineT", "0.2.0")
  def refine[P]: Refine[P, @@] = new Refine[P, @@]

  @deprecated("refineLit is deprecated in favor of refineMT", "0.2.0")
  def refineLit[P]: RefineM[P, @@] = new RefineM[P, @@]

  @deprecated("refineM is deprecated in favor of refineMV", "0.2.0")
  def refineM[P]: RefineM[P, Refined] = new RefineM[P, Refined]
}
