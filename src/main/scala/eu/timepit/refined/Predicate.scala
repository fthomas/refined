package eu.timepit.refined

trait Predicate[P, T] {
  def validate(x: T): Option[String]

  def msg(t: T): String = ""

  def isValid(t: T): Boolean =
    validate(t).isEmpty

  def notValid(t: T): Boolean =
    !isValid(t)
}

object Predicate {
  def apply[P, T](implicit p: Predicate[P, T]): Predicate[P, T] = p
}
