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
  implicit def autoInfer[T, A, B, F[_, _]](ta: F[T, A])(
    implicit
    ir: A ==> B, w: Wrapper[F]
  ): F[T, B] = macro InferM.macroImpl[T, A, B, F]

  implicit def autoRefineV[T, P](t: T)(
    implicit
    p: Predicate[P, T], w: Wrapper[Refined]
  ): Refined[T, P] = macro RefineM.macroImpl[P, T, Refined]

  implicit def autoRefineT[T, P](t: T)(
    implicit
    p: Predicate[P, T], w: Wrapper[@@]
  ): T @@ P = macro RefineM.macroImpl[P, T, @@]
}
