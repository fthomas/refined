package eu.timepit.refined

trait Inference[A, B] {
  def isValid: Boolean
}

object Inference {
  def instance[A, B](isValidV: Boolean): Inference[A, B] =
    new Inference[A, B] {
      def isValid: Boolean = isValidV
    }

  def alwaysTrue[A, B]: Inference[A, B] =
    instance(true)
}
