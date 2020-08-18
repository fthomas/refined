package eu.timepit.refined

import _root_.scopt.Read
import eu.timepit.refined.api.{RefType, Validate}

package object scopt {

  implicit def refTypeRead[F[_, _], T, P](implicit
      read: Read[T],
      refType: RefType[F],
      validate: Validate[T, P]
  ): Read[F[T, P]] = read.map(t => refType.refine[P].unsafeFrom(t))

}
