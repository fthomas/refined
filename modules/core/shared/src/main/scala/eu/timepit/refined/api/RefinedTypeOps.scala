package eu.timepit.refined
package api

class RefinedTypeOps[FTP, T](implicit rt: RefinedType.AuxT[FTP, T]) {

  def apply[F[_, _], P](t: T)(
      implicit ev: F[T, P] =:= FTP,
      rt: RefType[F],
      v: Validate[T, P]
  ): FTP =
    macro macros.RefineMacro.implApplyRef[FTP, F, T, P]

  def from(t: T): Either[String, FTP] =
    rt.refine(t)

  def unapply(t: T): Option[FTP] =
    from(t).right.toOption
}
