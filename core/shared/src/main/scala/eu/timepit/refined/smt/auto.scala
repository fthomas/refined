package eu.timepit.refined
package smt

import eu.timepit.refined.api.RefType

object auto {

  implicit def autoInfer[F[_, _], T, A, B](ta: F[T, A])(
    implicit
    fab: (Formula[A], Formula[B]), rt: RefType[F]
  ): F[T, B] = macro SmtInferM.macroImpl[F, T, A, B]

  //
}
