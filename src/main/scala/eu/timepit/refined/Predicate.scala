package eu.timepit.refined

trait Predicate[P, X] {
  def validate(x: X): Option[String]

  def isValid(x: X): Boolean =
    validate(x).isEmpty

  def isInvalid(x: X): Boolean =
    validate(x).isDefined
}

object Predicate {
  def apply[P, X](implicit ev: Predicate[P, X]): Predicate[P, X] = ev
}
