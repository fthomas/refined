package eu.timepit.refined

import eu.timepit.refined.api.{ Refined, RefType, Validate }
import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.macros.{ InferMacro, RefineMacro }
import shapeless.tag.@@

/**
 * Module that provides automatic refinements and automatic conversions
 * between refined types (refinement subtyping) at compile-time.
 */
object auto {

  /**
   * Implicitly converts (at compile-time) a value of type `F[T, A]` to
   * `F[T, B]` if there is a valid inference `A ==> B`. If the
   * inference is invalid, compilation fails.
   *
   * Example: {{{
   * scala> import eu.timepit.refined.api.Refined
   *      | import eu.timepit.refined.auto.{ autoInfer, autoRefineV }
   *      | import eu.timepit.refined.numeric.Greater
   *
   * scala> val x: Int Refined Greater[W.`5`.T] = 100
   *
   * scala> x: Int Refined Greater[W.`0`.T]
   * res0: Int Refined Greater[W.`0`.T] = 100
   * }}}
   */
  implicit def autoInfer[F[_, _], T, A, B](ta: F[T, A])(
    implicit
    rt: RefType[F], ir: A ==> B
  ): F[T, B] = macro InferMacro.impl[F, T, A, B]

  /**
   * Implicitly unwraps the `T` from a value of type `F[T, P]` using the
   * `[[api.RefType]]` instance of `F`. This allows a `F[T, P]` to be
   * used as it were a subtype of `T`.
   *
   * Note: This conversion is not needed if `F[T, _] <: T` holds (which
   * is the case for `shapeless.tag.@@`, for example).
   */
  implicit def autoUnwrap[F[_, _], T](tp: F[T, _])(implicit rt: RefType[F]): T =
    rt.unwrap(tp)

  /**
   * Implicitly wraps (at compile-time) a value of type `T` in
   * `[[api.Refined]][T, P]` if `t` satisfies the predicate `P`. If it does
   * not satisfy the predicate, compilation fails.
   *
   * This is an implicit version of `[[refineMV]]`.
   */
  implicit def autoRefineV[T, P](t: T)(
    implicit
    rt: RefType[Refined], v: Validate[T, P]
  ): Refined[T, P] = macro RefineMacro.impl[Refined, T, P]

  /**
   * Implicitly tags (at compile-time) a value of type `T` with `P` if `t`
   * satisfies the predicate `P`. If it does not satisfy the predicate,
   * compilation fails.
   *
   * This is an implicit version of `[[refineMT]]`.
   */
  implicit def autoRefineT[T, P](t: T)(
    implicit
    rt: RefType[@@], v: Validate[T, P]
  ): T @@ P = macro RefineMacro.impl[@@, T, P]
}
