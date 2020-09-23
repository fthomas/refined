package eu.timepit.refined

import _root_.scalaz.{@@, Contravariant, Equal, MonadError, Show}
import eu.timepit.refined.api.{RefType, Validate}

package object scalaz {

  implicit val scalazTagRefType: RefType[@@] =
    new RefType[@@] {
      override def unsafeWrap[T, P](t: T): T @@ P =
        t.asInstanceOf[T @@ P]

      override def unwrap[T, P](tp: T @@ P): T =
        tp.asInstanceOf[T]

      override def unsafeRewrap[T, A, B](ta: T @@ A): T @@ B =
        ta.asInstanceOf[T @@ B]
    }

  /**
   * `Equal` instance for refined types that delegates to the `Equal`
   * instance of the base type.
   */
  implicit def refTypeEqual[F[_, _], T: Equal, P](implicit rt: RefType[F]): Equal[F[T, P]] =
    scalaz.derivation.refTypeViaContravariant[F, Equal, T, P]

  /**
   * `Show` instance for refined types that delegates to the `Show`
   * instance of the base type.
   */
  implicit def refTypeShow[F[_, _], T: Show, P](implicit rt: RefType[F]): Show[F[T, P]] =
    scalaz.derivation.refTypeViaContravariant[F, Show, T, P]

  @deprecated("Generic derivation instances have been moved into the `derivation` object", "0.9.4")
  def refTypeContravariant[R[_, _], F[_], A, B](implicit
      C: Contravariant[F],
      R: RefType[R],
      F: F[A]
  ): F[R[A, B]] =
    scalaz.derivation.refTypeViaContravariant[R, F, A, B]

  @deprecated("Generic derivation instances have been moved into the `derivation` object", "0.9.4")
  def refTypeMonadError[R[_, _], F[_], A, B](implicit
      M: MonadError[F, String],
      R: RefType[R],
      V: Validate[A, B],
      F: F[A]
  ): F[R[A, B]] =
    scalaz.derivation.refTypeViaMonadError[R, F, A, B]
}
