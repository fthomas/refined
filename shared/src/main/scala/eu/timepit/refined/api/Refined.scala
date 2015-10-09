package eu.timepit.refined
package api

/**
 * Wraps a value of type `T` that satisfies the predicate `P`. Instances of
 * this class can be created with `[[refineV]]` and `[[refineMV]]` which
 * verify that the wrapped value satisfies `P`.
 */
final case class Refined[T, P] private (get: T) extends AnyVal {

  // Prevent the creation of a synthetic copy method that subverts the
  // private constructor. See https://github.com/fthomas/refined/issues/57
  private def copy[T2, P2](t: T2): Refined[T2, P2] =
    new Refined(t)
}

object Refined {

  /**
   * Wraps `t` in `[[Refined]][T, P]` ''without'' verifying that it satisfies
   * the predicate `P`. This method is for internal use only.
   */
  def unsafeApply[T, P](t: T): Refined[T, P] =
    new Refined(t)
}
