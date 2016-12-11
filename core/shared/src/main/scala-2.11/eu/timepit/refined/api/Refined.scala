package eu.timepit.refined
package api

/**
 * Wraps a value of type `T` that satisfies the predicate `P`. Instances of
 * this class can be created with `[[refineV]]` and `[[refineMV]]` which
 * verify that the wrapped value satisfies `P`.
 */
final class Refined[T, P] private (val value: T) extends AnyVal with Serializable {

  @deprecated("`get` has been renamed to `value` to avoid confusion with `Option#get`", "0.6.1")
  def get: T = value

  override def toString: String =
    value.toString
}

object Refined {

  /**
   * Wraps `t` in `[[Refined]][T, P]` ''without'' verifying that it satisfies
   * the predicate `P`. This method is for internal use only.
   */
  def unsafeApply[T, P](t: T): Refined[T, P] =
    new Refined(t)

  def unapply[T, P](r: Refined[T, P]): Some[T] =
    Some(r.value)
}
