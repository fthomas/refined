package eu.timepit.refined
package internal

import eu.timepit.refined.api.{ RefType, Unpack, Validate }

/**
 * Helper class that allows the types `F`, `T`, and `P` to be inferred
 * from calls like `[[api.RefType.applyRef]][F[T, P]](t)`.
 *
 * See [[http://tpolecat.github.io/2015/07/30/infer.html]] for a detailed
 * explanation of this trick.
 */
final class ApplyRefPartiallyApplied[FTP] {

  def apply[F[_, _], T, P](t: T)(
    implicit
    ev: F[T, P] =:= FTP, rt: RefType[F], v: Validate[T, P]
  ): Either[String, FTP] =
    rt.refine[P](t).right.map(ev)

  def apply2[T](t: T)(implicit u: Unpack.AuxT[FTP, T]): Either[String, FTP] =
    u.refType.refine[u.P](t)(u.validate).right.map(u.pack)

  def unsafeFrom[F[_, _], T, P](t: T)(
    implicit
    ev: F[T, P] =:= FTP, rt: RefType[F], v: Validate[T, P]
  ): FTP =
    ev(rt.refine[P].unsafeFrom(t))
}
