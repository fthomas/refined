package eu.timepit.refined
package api

/**
 * Evidence that states if the conclusion `C` can be inferred from the
 * premise `P` or not.
 */
case class Inference[P, C](isValid: Boolean, show: String) {

  def adapt[P2, C2](adaptedShow: String): Inference[P2, C2] =
    copy(show = adaptedShow.format(show))

  final def notValid: Boolean =
    !isValid
}

object Inference {

  type ==>[P, C] = Inference[P, C]

  def apply[P, C](implicit ir: Inference[P, C]): Inference[P, C] = ir

  def alwaysValid[P, C](show: String): Inference[P, C] =
    Inference(isValid = true, show)

  def combine[P1, P2, P, C1, C2, C](r1: Inference[P1, C1], r2: Inference[P2, C2], show: String): Inference[P, C] =
    Inference(r1.isValid && r2.isValid, show.format(r1.show, r2.show))
}

case class TypedInference[T, P, C](isValid: Boolean, show: String) {

  final def notValid: Boolean =
    !isValid
}

object TypedInference {

  def apply[T, P, C](implicit ti: TypedInference[T, P, C]): TypedInference[T, P, C] = ti

  implicit def fromUntyped[T, P, C](implicit i: Inference[P, C]): TypedInference[T, P, C] =
    TypedInference(i.isValid, i.show)
}
