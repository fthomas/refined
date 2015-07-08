package eu.timepit.refined

/**
 * Evidence that states if the conclusion `C` can be inferred from the
 * premise `P` or not.
 */
case class InferenceRule[P, C](isValid: Boolean, show: String) {

  def adapt[P2, C2](adaptedShow: String): InferenceRule[P2, C2] =
    copy(show = adaptedShow.format(show))

  final def notValid: Boolean =
    !isValid
}

object InferenceRule {

  def apply[P, C](implicit ir: InferenceRule[P, C]): InferenceRule[P, C] = ir

  def alwaysValid[P, C](show: String): InferenceRule[P, C] =
    InferenceRule(isValid = true, show)

  def combine[P1, P2, P, C1, C2, C](r1: InferenceRule[P1, C1], r2: InferenceRule[P2, C2], show: String): InferenceRule[P, C] =
    InferenceRule(r1.isValid && r2.isValid, show.format(r1.show, r2.show))
}

private[refined] object InferenceRuleAlias {

  type ==>[P, C] = InferenceRule[P, C]
}
