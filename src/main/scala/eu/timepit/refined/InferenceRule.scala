package eu.timepit.refined

/**
 * Evidence that states if the conclusion `C` can be inferred from the
 * premise `P` or not.
 */
case class InferenceRule[P, C](isValid: Boolean) {

  def adapted[P2, C2]: InferenceRule[P2, C2] =
    copy()

  def &&[P2, P3, C2, C3](other: InferenceRule[P2, C2]): InferenceRule[P3, C3] =
    copy(isValid && other.isValid)

  final def notValid: Boolean =
    !isValid
}

object InferenceRule {

  def apply[P, C](implicit i: InferenceRule[P, C]): InferenceRule[P, C] = i

  def alwaysValid[P, C]: InferenceRule[P, C] =
    InferenceRule(true)
}

object InferenceRuleAlias {

  type ==>[P, C] = InferenceRule[P, C]
}
