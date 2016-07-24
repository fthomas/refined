package eu.timepit.refined
package api

trait RefinedType[FTP] {
  type F[_, _]
  type T
  type P

  def refType: RefType[F]

  def validate: Validate[T, P]

  def subst1: F[T, P] =:= FTP

  def subst2: FTP =:= F[T, P]

  def refine(t: T): Either[String, FTP] = ???

  def refineUnsafe(t: T): FTP = ???

  def refineM(t: T): FTP = ???
}

object RefinedType {

  def apply[FTP](implicit rt: RefinedType[FTP]): Aux[FTP, rt.F, rt.T, rt.P] = rt

  type Aux[FTP, F0[_, _], T0, P0] = RefinedType[FTP] {
    type F[x, y] = F0[x, y]
    type T = T0
    type P = P0
  }

  type AuxT[FTP, T0] = RefinedType[FTP] {
    type T = T0
  }

  implicit def instance[F0[_, _], T0, P0](
    implicit
    rt: RefType[F0], v: Validate[T0, P0]
  ): Aux[F0[T0, P0], F0, T0, P0] =
    new RefinedType[F0[T0, P0]] {
      override type F[x, y] = F0[x, y]
      override type T = T0
      override type P = P0

      override def refType: RefType[F] = rt

      override def validate: Validate[T, P] = v

      override def subst1: F[T, P] =:= F0[T, P0] = implicitly

      override def subst2: F0[T0, P0] =:= F[T, P] = implicitly
    }
}
