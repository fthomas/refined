package eu.timepit.refined

/**
 * Type class for validating if the conclusion `C` can be inferred from the
 * premise `P`.
 */
trait InferenceRule[P, C] {
  def isValid: Boolean

  def adapted[P2, C2]: InferenceRule[P2, C2] =
    InferenceRule.instance(isValid)
}

object InferenceRule {
  def instance[P, C](valid: Boolean): InferenceRule[P, C] =
    new InferenceRule[P, C] {
      def isValid: Boolean = valid
    }

  def alwaysValid[P, C]: InferenceRule[P, C] =
    instance(true)
}
