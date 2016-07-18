package eu.timepit.refined
package api

trait RefineOps[FTP] {
  def refine[T](t: T)(implicit u: Unpack.AuxT[FTP, T]): Either[String, FTP] =
    u.refType.refine(t)(u.validate).right.map(u.pack)

  def refine2[F[_, _], T, P](t: T)(implicit ev: F[T, P] =:= FTP, rt: RefType[F], v: Validate[T, P]): Either[String, FTP] =
    rt.refine[P](t).right.map(ev)

  def refineUnsafe[T](t: T)(implicit u: Unpack.AuxT[FTP, T]): FTP =
    u.pack(u.refType.refine.unsafeFrom(t)(u.validate))

  def refineM[F[_, _], T, P](t: T)(implicit ev: F[T, P] =:= FTP, rt: RefType[F], v: Validate[T, P]): FTP = macro macros.RefineMacro.implApplyRef[FTP, F, T, P]
}

object RefineOps {
  def apply[FTP]: RefineOps[FTP] = new RefineOps[FTP] {}
}

object Natural extends RefineOps[Natural]

trait Unpack[FTP] {
  type F[_, _]
  type T
  type P

  def refType: RefType[F]
  def validate: Validate[T, P]

  def pack: F[T, P] =:= FTP
}

object Unpack {

  def apply[FTP](implicit u: Unpack[FTP]): Aux[FTP, u.F, u.T, u.P] = u

  type Aux[FTP, F0[_, _], T0, P0] = Unpack[FTP] {
    type F[x, y] = F0[x, y]
    type T = T0
    type P = P0
  }

  type AuxT[FTP, T0] = Unpack[FTP] {
    type T = T0
  }

  implicit def instance[F0[_, _], T0, P0](
    implicit
    rt: RefType[F0], v: Validate[T0, P0]
  ): Aux[F0[T0, P0], F0, T0, P0] =
    new Unpack[F0[T0, P0]] {
      override type F[x, y] = F0[x, y]
      override type T = T0
      override type P = P0

      override def refType: RefType[F] = rt
      override def validate: Validate[T, P] = v

      override def pack: F[T, P] =:= F0[T, P0] = implicitly
    }
}
