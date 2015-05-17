package eu.timepit.refined

/**
 * Type class for validating values of type `T` according to a type-level
 * predicate `P`. The semantics of `P` are defined by the instance of this
 * type class for `P`.
 */
trait Predicate[P, T] {
  /** Checks if `t` satisfies the predicate `P`. */
  def isValid(t: T): Boolean

  /** Returns a string representation of this [[Predicate]] using `t`. */
  def show(t: T): String

  /**
   * Returns `None` if `t` satisfies the predicate `P`, or an error message
   * contained in `Some` otherwise.
   */
  def validated(t: T): Option[String] =
    if (isValid(t)) None else Some(s"Predicate failed: ${show(t)}.")

  /** Checks if `t` does not satisfy the predicate `P`. */
  def notValid(t: T): Boolean =
    !isValid(t)
}

object Predicate {
  def apply[P, T](implicit p: Predicate[P, T]): Predicate[P, T] = p

  /** Constructs a [[Predicate]] from its parameters. */
  def instance[P, T](isValidF: T => Boolean, showF: T => String): Predicate[P, T] =
    new Predicate[P, T] {
      def isValid(t: T): Boolean = isValidF(t)
      def show(t: T): String = showF(t)
    }

  /** Returns a [[Predicate]] that ignores its inputs and always yields `true`. */
  def alwaysTrue[P, T]: Predicate[P, T] =
    Predicate.instance(_ => true, _ => "true")

  /** Returns a [[Predicate]] that ignores its inputs and always yields `false`. */
  def alwaysFalse[P, T]: Predicate[P, T] =
    Predicate.instance(_ => false, _ => "false")
}
