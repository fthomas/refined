package eu.timepit.refined
package internal

import eu.timepit.refined.api.{ RefType, Validate }

/**
 * TODO
 */
class ApplyRefAux[FTP] {

  def apply[F[_, _], T, P](t: T)(
    implicit
    ev: FTP =:= F[T, P], rt: RefType[F], v: Validate[T, P]
  ): Either[String, F[T, P]] =
    rt.refine[P](t)
}
