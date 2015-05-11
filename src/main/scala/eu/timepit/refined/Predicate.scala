package eu.timepit.refined

trait Predicate[P, T] {
  def isValid(t: T): Boolean

  def show(t: T): String

  def notValid(t: T): Boolean =
    !isValid(t)
}

object Predicate {
  def apply[P, T](implicit p: Predicate[P, T]): Predicate[P, T] = p
}
