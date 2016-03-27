package eu.timepit.refined

import eu.timepit.refined.api.{ Refined, RefType, TypedInference, Validate }
import eu.timepit.refined.macros.{ InferMacro, RefineMacro }
import shapeless.tag.@@

trait auto {

  /**
   * Implicitly converts (at compile-time) a value of type `F[T, A]` to
   * `F[T, B]` if there is a valid inference `A ==> B`. If the
   * inference is invalid, compilation fails.
   */
  implicit def autoInfer[F[_, _], T, A, B](ta: F[T, A])(
    implicit
    rt: RefType[F], ti: TypedInference[T, A, B]
  ): F[T, B] = macro InferMacro.impl[F, T, A, B]

  /**
   * Implicitly unwraps the `T` from a value of type `F[T, P]` using the
   * `[[api.RefType]]` instance of `F`. This allows a `F[T, P]` to be
   * used as it were a subtype of `T`.
   *
   * Note: This conversion is not needed if `F[T, _] <: T` holds (which
   * is the case for `shapeless.tag.@@`, for example).
   */
  implicit def autoUnwrap[F[_, _], T, P](tp: F[T, P])(implicit rt: RefType[F]): T =
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

object auto extends auto

@deprecated("The 'implicits' object has been deprecated in favor of the 'auto' object.", "0.2.4")
object implicits extends auto
