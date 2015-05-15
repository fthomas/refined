package eu.timepit.refined

trait Predicate[P, T] {
  def isValid(t: T): Boolean

  def show(t: T): String

  def validated(t: T): Option[String] =
    if (isValid(t)) None else Some(s"Predicate failed: ${show(t)}.")

  def notValid(t: T): Boolean =
    !isValid(t)
}

object Predicate {
  def apply[P, T](implicit p: Predicate[P, T]): Predicate[P, T] = p

  def instance[P, T](isValidF: T => Boolean, showF: T => String): Predicate[P, T] =
    new Predicate[P, T] {
      def isValid(t: T): Boolean = isValidF(t)
      def show(t: T): String = showF(t)
    }
}
