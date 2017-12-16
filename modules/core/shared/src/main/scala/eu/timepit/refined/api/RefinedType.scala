package eu.timepit.refined
package api

trait RefinedType[FTP] {
  type F[_, _]
  type T
  type P

  val refType: RefType[F]

  val validate: Validate[T, P]

  val alias: F[T, P] =:= FTP

  final def refine(t: T): Either[String, FTP] = {
    val res = validate.validate(t)
    if (res.isPassed) Right(alias(refType.unsafeWrap(t)))
    else Left(validate.showResult(t, res))
  }

  final def unsafeRefine(t: T): FTP =
    refine(t).fold(err => throw new IllegalArgumentException(err), identity)
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
      rt: RefType[F0],
      v: Validate[T0, P0]
  ): Aux[F0[T0, P0], F0, T0, P0] =
    new RefinedType[F0[T0, P0]] {
      override type F[x, y] = F0[x, y]
      override type T = T0
      override type P = P0

      override val refType: RefType[F] = rt
      override val validate: Validate[T, P] = v
      override val alias: F[T, P] =:= F0[T0, P0] = implicitly
    }
}
