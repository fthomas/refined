package eu.timepit.refined

import eu.timepit.refined.InferenceRule.==>
import eu.timepit.refined.internal._
import shapeless.tag.@@

object implicits {

  /**
   * Implicitly converts (at compile-time) a value of type `F[T, A]` to
   * `F[T, B]` if there is a valid inference rule `A ==> B`. If the
   * inference rule is invalid, compilation fails.
   */
  implicit def autoInfer[F[_, _], T, A, B](ta: F[T, A])(
    implicit
    ir: A ==> B, rt: RefType[F]
  ): F[T, B] = macro InferM.macroImpl[F, T, A, B]

  /**
   * Implicitly wraps (at compile-time) a value of type `T` in
   * `[[Refined]][T, P]` if `t` satisfies the predicate `P`. If it does not
   * satisfy the predicate, compilation fails.
   *
   * This is an implicit version of `[[refineMV]]`.
   */
  implicit def autoRefineV[T, P](t: T)(
    implicit
    p: Predicate[P, T], rt: RefType[Refined]
  ): Refined[T, P] = macro RefineMAux.macroImpl[Refined, T, P]

  /**
   * Implicitly tags (at compile-time) a value of type `T` with `P` if `t`
   * satisfies the predicate `P`. If it does not satisfy the predicate,
   * compilation fails.
   *
   * This is an implicit version of `[[refineMT]]`.
   */
  implicit def autoRefineT[T, P](t: T)(
    implicit
    p: Predicate[P, T], rt: RefType[@@]
  ): T @@ P = macro RefineMAux.macroImpl[@@, T, P]
}
