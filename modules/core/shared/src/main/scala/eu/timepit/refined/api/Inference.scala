package eu.timepit.refined.api

/**
 * Evidence that states if the conclusion `C` can be inferred from the
 * premise `P` or not.
 *
 * This trait is used to implement refinement subtyping.
 */
sealed trait Inference[P, C] {
  def isValid: Boolean
  def show: String
  def adapt[P2, C2](adaptedShow: String): Inference[P2, C2]
  final def notValid: Boolean =
    !isValid
}

object Inference {

  /**
   * This type class is used to implement refinement subtyping. If a
   * `ValidInference[P, C]` exists, the type `F[T, P]` is considered a subtype
   * of `F[T, C]`.
   */
  final case class InferAlways[P, C](show: String) extends Inference[P, C] {

    override def adapt[P2, C2](adaptedShow: String): Inference[P2, C2] =
      copy(show = adaptedShow.format(show))

    override def isValid: Boolean = true
  }

  /**
   * This type class is used to implement refinement subtyping. If an inference
   * `InferWhen[P, C]` with an ''isValid'' field set to true exists,
   * the type `F[T, P]` is considered a subtype of `F[T, C]`.
   */
  final case class InferWhen[P, C](isValid: Boolean, show: String) extends Inference[P, C] {

    override def adapt[P2, C2](adaptedShow: String): Inference[P2, C2] =
      copy(show = adaptedShow.format(show))
  }

  /**
   * This type class is used to define an invalid refinement subtyping. If an inference
   * `ValidNever[P, C]`  exists, the type `F[T, P]` is NOT considered a subtype of `F[T, C]`.
   */
  final case class InferNever[P, C](show: String) extends Inference[P, C] {

    override def adapt[P2, C2](adaptedShow: String): Inference[P2, C2] =
      copy(show = adaptedShow.format(show))

    override def isValid: Boolean = false

  }

  type ==>[P, C] = Inference[P, C]

  type ===>[P, C] = InferAlways[P, C]

  def apply[P, C](implicit i: Inference[P, C]): Inference[P, C] = i

  def apply[P, C](isValid: Boolean, show: String): Inference[P, C] =
    if (isValid) InferAlways(show)
    else InferNever(show)

  def alwaysValid[P, C](show: String): InferAlways[P, C] =
    InferAlways(show)

  def combine[P1, P2, P, C1, C2, C](i1: Inference[P1, C1],
                                    i2: Inference[P2, C2],
                                    show: String): Inference[P, C] =
    Inference(i1.isValid && i2.isValid, show.format(i1.show, i2.show))
}
