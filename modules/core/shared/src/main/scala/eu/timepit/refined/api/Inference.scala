package eu.timepit.refined.api

/**
 * Evidence that states if the conclusion `C` can be inferred from the
 * premise `P` or not.
 *
 * This type class is used to implement refinement subtyping. If a valid
 * `Inference[P, C]` exists, the type `F[T, P]` is considered a subtype
 * of `F[T, C]`.
 */
case class Inference[P, C](show: String) {

  final def adapt[P2, C2](adaptedShow: String): Inference[P2, C2] =
    copy(show = adaptedShow.format(show))

}

object Inference {

  type ==>[P, C] = Inference[P, C]

  def apply[P, C](implicit i: Inference[P, C]): Inference[P, C] = i

  def alwaysValid[P, C](show: String): Inference[P, C] =
    Inference(show)

  def combine[P1, P2, P, C1, C2, C](
      i1: Inference[P1, C1],
      i2: Inference[P2, C2],
      show: String
  ): Inference[P, C] =
    Inference(show.format(i1.show, i2.show))
}
