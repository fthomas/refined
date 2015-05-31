package eu.timepit

import eu.timepit.refined.internal.{ Infer, Refine, RefineLit }
import shapeless.tag.@@

package object refined {
  val W = shapeless.Witness

  def refine[P]: Refine[P] = new Refine[P]
  def refineLit[P]: RefineLit[P] = new RefineLit[P]

  implicit def infer[T, A, B](t: T @@ A)(implicit i: Inference[A, B]): T @@ B = macro Infer.macroImpl[T, A, B]
}
