package eu.timepit.refined
package internal

import shapeless.tag.@@

/**
 * Helper class that allows the type `T` to be inferred from calls like
 * `[[refine]][P](t)`.
 */
final class Refine[P] {
  def apply[T](t: T)(implicit p: Predicate[P, T]): Either[String, T @@ P] =
    p.refine(t)
}
