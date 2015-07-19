package eu.timepit.refined

import eu.timepit.refined.internal._
import shapeless.tag.@@

object implicits {

  implicit def autoInferV[T, A, B](t: Refined[T, A])(
    implicit
    ir: InferenceRule[A, B], w: Wrapper[Refined]
  ): Refined[T, B] = macro InferM.macroImpl[T, A, B, Refined]

  implicit def autoInferT[T, A, B](t: T @@ A)(
    implicit
    ir: InferenceRule[A, B], w: Wrapper[@@]
  ): T @@ B = macro InferM.macroImpl[T, A, B, @@]

  implicit def autoRefineV[T, P](t: T)(
    implicit
    p: Predicate[P, T], w: Wrapper[Refined]
  ): Refined[T, P] = macro RefineM.macroImpl[P, T, Refined]

  implicit def autoRefineT[T, P](t: T)(
    implicit
    p: Predicate[P, T], w: Wrapper[@@]
  ): T @@ P = macro RefineM.macroImpl[P, T, @@]
}
