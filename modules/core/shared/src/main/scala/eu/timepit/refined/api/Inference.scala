package eu.timepit.refined.api

/**
 * Evidence that states if the conclusion `C` can be inferred from the
 * premise `P` or not.
 *
 * This trait is used to implement refinement subtyping.
 */
trait Inference[P, C] {
  type T[P2, C2] <: Inference[P2, C2]
  def isValid: Boolean
  final def notValid: Boolean = !isValid
  def show: String
  def adapt[P2, C2](adaptedShow: String): T[P2, C2]

}

/**
 * This type class is used to implement compile-time refinement subtyping. If a
 * `InferAlways[P, C]` exists, the type `F[T, P]` is considered a subtype
 * of `F[T, C]`.
 */
final case class InferAlways[P, C] private (show: String) extends Inference[P, C] {

  type T[P2, C2] = InferAlways[P2, C2]
  override def isValid: Boolean = true
  override def adapt[P2, C2](adaptedShow: String): T[P2, C2] = copy(show = adaptedShow.format(show))
}

/**
 * This type class is used to define a runtime refinement subtyping. If an inference
 * `InferWhen[P, C]`  exists, and ''isValid'' is true, the type `F[T, P]` is considered a subtype of `F[T, C]`.
 */
final case class InferWhen[P, C] private (isValid: Boolean, show: String) extends Inference[P, C] {

  type T[P2, C2] = InferWhen[P2, C2]
  override def adapt[P2, C2](adaptedShow: String): T[P2, C2] = copy(show = adaptedShow.format(show))
}

object Inference {

  type ==>[P, C] = InferAlways[P, C]
  type ?=>[P, C] = InferWhen[P, C]

  def apply[P, C](implicit i: Inference[P, C]): Inference[P, C] = i

  def alwaysValid[P, C](show: String): P ==> C =
    InferAlways(show)

  def apply[P, C](isValid: Boolean, show: String): P ?=> C =
    InferWhen(isValid, show)

  def combine[P1, P2, P, C1, C2, C](i1: P1 ==> C1, i2: P2 ==> C2, show: String): P ==> C =
    alwaysValid(show)

  def combine[P1, P2, P, C1, C2, C](i1: Inference[P1, C1],
                                    i2: Inference[P2, C2],
                                    show: String): P ?=> C =
    Inference(i1.isValid && i2.isValid, show.format(i1.show, i2.show))
}
