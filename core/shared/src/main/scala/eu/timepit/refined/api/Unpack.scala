package eu.timepit.refined
package api


trait RefineOps[FTP] {
  def refine[T](t: T)(implicit u: Unpack[T, FTP]): Either[String, FTP] =
    u.refType.refine(t)(u.validate).right.map(u.subst2)

  def refineM[F[_, _], T, P](t: T)(implicit ev: F[T, P] =:= FTP, rt: RefType[F], v: Validate[T, P]): FTP = macro macros.RefineMacro.implApplyRef[FTP, F, T, P]
}

object RefineOps {
  def apply[FTP]: RefineOps[FTP] = new RefineOps[FTP] {}
}

trait Unpack[T, FTP] {
  type F[_, _]
  type P

  def refType: RefType[F]
  def validate: Validate[T, P]

  def subst: FTP => F[T, P]
  def subst2: F[T, P] => FTP
}

object Unpack {

  def apply[T, FTP](implicit u: Unpack[T, FTP]): Aux[T, FTP, u.F, u.P] = u

  type Aux[T, FTP, F0[_, _], P0] = Unpack[T, FTP] {
    type F[x, y] = F0[x, y]
    type P = P0
  }

  implicit def instance[T, F0[_, _], P0](
    implicit
    rt: RefType[F0], v: Validate[T, P0]
  ): Aux[T, F0[T, P0], F0, P0] =
    new Unpack[T, F0[T, P0]] {
      override type F[x, y] = F0[x, y]
      override type P = P0

      override def refType: RefType[F] = rt
      override def validate: Validate[T, P] = v

      override def subst: F0[T, P0] => F[T, P] = identity
      override def subst2: F[T, P] => F0[T, P0] = identity
    }
}
