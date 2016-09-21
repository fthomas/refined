package eu.timepit.refined.scalaz

import eu.timepit.refined.api.{RefType, Validate}
import eu.timepit.refined.macros.RefineMacro
import scalaz.@@

object auto {

  /**
   * Implicitly tags (at compile-time) a value of type `T` with `P` if `t`
   * satisfies the predicate `P`. If it does not satisfy the predicate,
   * compilation fails.
   */
  implicit def autoRefineScalazTag[T, P](t: T)(
      implicit rt: RefType[@@],
      v: Validate[T, P]
  ): T @@ P = macro RefineMacro.impl[@@, T, P]
}
