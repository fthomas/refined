package eu.timepit.refined.api

import eu.timepit.refined.macros

class RefinedTypeOps[FTP, F[_, _], T, P](
    implicit ev: F[T, P] =:= FTP,
    rt: RefType[F],
    v: Validate[T, P]
) {

  def apply(t: T)(
      implicit ev: F[T, P] =:= FTP,
      rt: RefType[F],
      v: Validate[T, P]
  ): FTP =
    macro macros.RefineMacro.implApplyRef[FTP, F, T, P]

  def from(t: T): Either[String, FTP] =
    rt.refine[P](t).map(ev)

  def unapply(t: T): Option[FTP] =
    from(t).toOption
}
