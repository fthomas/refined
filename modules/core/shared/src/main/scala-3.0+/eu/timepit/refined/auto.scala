package eu.timepit.refined

import eu.timepit.refined.api.RefType

/**
 * Module that provides automatic refinements and automatic conversions
 * between refined types (refinement subtyping) at compile-time.
 */
object auto {

  /**
   * Implicitly unwraps the `T` from a value of type `F[T, P]` using the
   * `[[api.RefType]]` instance of `F`. This allows a `F[T, P]` to be
   * used as it were a subtype of `T`.
   *
   * Example: {{{
   * scala> import eu.timepit.refined.auto.autoUnwrap
   *      | import eu.timepit.refined.types.numeric.PosInt
   *
   * scala> def plusOne(i: Int): Int = i + 1
   *      | val x = PosInt.unsafeFrom(42)
   *
   * // converts x implicitly to an Int:
   * scala> plusOne(x)
   * res0: Int = 43
   * }}}
   *
   * Note: This conversion is not needed if `F[T, _] <: T` holds (which
   * is the case for `shapeless.tag.@@`, for example).
   */
  implicit def autoUnwrap[F[_, _], T, P](tp: F[T, P])(implicit rt: RefType[F]): T =
    rt.unwrap(tp)
}
