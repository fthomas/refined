package eu.timepit.refined.api

import eu.timepit.refined.internal.Resources
import scala.util.Try
import scala.util.control.NonFatal

/**
 * Type class for validating values of type `T` according to a type-level
 * predicate `P`. The semantics of `P` are defined by the instance(s) of
 * this type class for `P`.
 */
trait Validate[T, P] extends Serializable {

  type R

  final type Res = Result[R]

  def validate(t: T): Res

  /** Get `t` string representation. */
  def serializeValue(t: T): String

  /** Returns a string representation of this `[[Validate]]` using `t` string representation. */
  def constructExpr(valueString: String): String

  final def description: String = constructExpr("x")

  def showResult(t: T, r: Res): String =
    Resources.predicateResultDetailDot(r, showExpr(t))

  /** Returns a string representation of this `[[Validate]]` using `t`. */
  final def showExpr(t: T): String = constructExpr(serializeValue(t))

  /** Checks if `t` satisfies the predicate `P`. */
  final def isValid(t: T): Boolean =
    validate(t).isPassed

  /** Checks if `t` does not satisfy the predicate `P`. */
  final def notValid(t: T): Boolean =
    validate(t).isFailed

  /**
   * Returns the result of `[[showExpr]]` in a `List`. Can be overridden
   * to accumulate the string representations of sub-predicates.
   */
  def accumulateShowExpr(t: T): List[String] =
    List(showExpr(t))

  private[refined] def contramap[U](f: U => T): Validate.Aux[U, P, R] = {
    val self: Validate.Aux[T, P, R] = this
    new Validate[U, P] {
      override type R = self.R
      override def validate(u: U): Res = self.validate(f(u))
      override def constructExpr(uString: String): String = self.constructExpr(uString)
      override def serializeValue(u: U): String = self.serializeValue(f(u))
      override def showResult(u: U, r: Res): String = self.showResult(f(u), r)
      override def accumulateShowExpr(u: U): List[String] = self.accumulateShowExpr(f(u))
    }
  }
}

object Validate {

  type Aux[T, P, R0] = Validate[T, P] { type R = R0 }

  type Plain[T, P] = Aux[T, P, P]

  def apply[T, P](implicit v: Validate[T, P]): Aux[T, P, v.R] = v

  /** Constructs a `[[Validate]]` from its parameters. */
  def instance[T, P, R0](f: T => Result[R0], serializer: T => String, expr: String => String): Aux[T, P, R0] =
    new Validate[T, P] {
      override type R = R0
      override def validate(t: T): Res = f(t)
      override def serializeValue(t: T): String = expr(t)
      override def constructExpr(valueString: String): String = expr(valueString)
    }

  /** Constructs a constant `[[Validate]]` from its parameters. */
  def constant[T, P, R](isValidV: Result[R], showV: String): Aux[T, P, R] =
    instance(_ => isValidV, _ => "will not be used", _ => showV)

  /**
   * Constructs a `[[Validate]]` from the predicate `f`. All values of type
   * `T` for which `f` returns `true` are considered valid according to `P`.
   */
  def fromPredicate[T, P](f: T => Boolean, serializeValue: T => String, constructExpr: String => String, p: P): Plain[T, P] = {
    val g = serializeValue
    val h = constructExpr
    new Validate[T, P] {
      override type R = P
      override def validate(t: T): Res = Result.fromBoolean(f(t), p)
      override def serializeValue(t: T): String = g(t)
      override def constructExpr(tString: String): String = h(tString)
    }
  }

  /**
   * Constructs a `[[Validate]]` from the partial function `pf`. All `T`s for
   * which `pf` throws an exception are considered invalid according to `P`.
   */
  def fromPartial[T, U, P](pf: T => U, name: String, p: P): Plain[T, P] =
    new Validate[T, P] {
      override type R = P

      override def validate(t: T): Res =
        try {
          pf(t)
          Passed(p)
        } catch {
          case NonFatal(_) => Failed(p)
        }

      override def constructExpr(tString: String): String =
        Resources.isValidName(name, tString)

      override def serializeValue(t: T): String = s"$t"

      override def showResult(t: T, res: Res): String =
        Resources.namePredicateResultMessage(name, res, Try(pf(t)))
    }

  /** Returns a `[[Validate]]` that ignores its input and always yields `[[api.Passed]]`. */
  def alwaysPassed[T, P, R](r: R): Aux[T, P, R] =
    constant(Passed(r), "true")

  /** Returns a `[[Validate]]` that ignores its input and always yields `[[api.Failed]]`. */
  def alwaysFailed[T, P, R](r: R): Aux[T, P, R] =
    constant(Failed(r), "false")
}
