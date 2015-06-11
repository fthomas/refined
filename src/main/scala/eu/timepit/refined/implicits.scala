package eu.timepit.refined

import eu.timepit.refined.internal._
import shapeless.tag.@@

object implicits {
  implicit def autoInfer[T, A, B](t: T @@ A)(implicit i: InferenceRule[A, B]): T @@ B = macro Infer.macroImpl[T, A, B]
  implicit def autoRefine[T, P](t: T)(implicit p: Predicate[P, T]): T @@ P = macro RefineLit.macroImpl[P, T]
}
