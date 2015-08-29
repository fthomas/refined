package eu.timepit.refined

import scala.util.{Failure, Success, Try}

/**
 * Type class for validating values of type `T` according to a type-level
 * predicate `P`. The semantics of `P` are defined by the instance(s) of
 * this type class for `P`.
 */
trait Predicate[P, T] extends Serializable { self =>

  /** Checks if `t` satisfies the predicate `P`. */
  def isValid(t: T): Boolean

  /** Returns a string representation of this [[Predicate]] using `t`. */
  def show(t: T): String

  /**
   * Returns `None` if `t` satisfies the predicate `P`, or an error message
   * contained in `Some` otherwise.
   */
  def validate(t: T): Option[String] =
    if (isValid(t)) None else Some(s"Predicate failed: ${show(t)}.")

  /**
   * Denotes whether this [[Predicate]] is constant (which is false by
   * default). A constant [[Predicate]] ignores the argument passed to
   * [[isValid]].
   */
  val isConstant: Boolean = false

  /** Checks if `t` does not satisfy the predicate `P`. */
  final def notValid(t: T): Boolean =
    !isValid(t)

  /**
   * Returns the result of [[isValid]] in a `List`. Can be overridden to
   * accumulate the results of sub-predicates.
   */
  def accumulateIsValid(t: T): List[Boolean] =
    List(isValid(t))

  /**
   * Returns the result of [[show]] in a `List`. Can be overridden to
   * accumulate the string representations of sub-predicates.
   */
  def accumulateShow(t: T): List[String] =
    List(show(t))

  private[refined] def contramap[U](f: U => T): Predicate[P, U] =
    new Predicate[P, U] {
      def isValid(u: U): Boolean = self.isValid(f(u))
      def show(u: U): String = self.show(f(u))
      override def validate(u: U): Option[String] = self.validate(f(u))
      override val isConstant: Boolean = self.isConstant
      override def accumulateIsValid(u: U): List[Boolean] = self.accumulateIsValid(f(u))
      override def accumulateShow(u: U): List[String] = self.accumulateShow(f(u))
    }
}

object Predicate {

  def apply[P, T](implicit p: Predicate[P, T]): Predicate[P, T] = p

  /** Constructs a [[Predicate]] from its parameters. */
  def instance[P, T](isValidF: T => Boolean, showF: T => String, constant: Boolean = false): Predicate[P, T] =
    new Predicate[P, T] {
      def isValid(t: T): Boolean = isValidF(t)
      def show(t: T): String = showF(t)
      override val isConstant: Boolean = constant
    }

  /** Constructs a constant [[Predicate]] from its parameters. */
  def constant[P, T](isValidV: Boolean, showV: String): Predicate[P, T] =
    instance(_ => isValidV, _ => showV, constant = true)

  /**
   * Constructs a [[Predicate]] from the partial function `pf`. All `T`s for
   * which `pf` throws an exception are considered invalid according to `P`.
   */
  def fromPartial[P, T, U](pf: T => U, name: String): Predicate[P, T] =
    new Predicate[P, T] {
      def isValid(t: T): Boolean = Try(pf(t)).isSuccess
      def show(t: T): String = s"""isValid$name("$t")"""

      override def validate(t: T): Option[String] =
        Try(pf(t)) match {
          case Success(_) => None
          case Failure(ex) => Some(s"$name predicate failed: ${ex.getMessage}")
        }
    }

  /** Returns a [[Predicate]] that ignores its inputs and always yields `true`. */
  def alwaysValid[P, T]: Predicate[P, T] =
    constant(isValidV = true, "true")

  /** Returns a [[Predicate]] that ignores its inputs and always yields `false`. */
  def alwaysInvalid[P, T]: Predicate[P, T] =
    constant(isValidV = false, "false")
}
