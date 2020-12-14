package eu.timepit.refined

import _root_.org.rogach.scallop.ValueConverter
import eu.timepit.refined.api.{RefType, Validate}

package object scallop {

  implicit def refTypeConverter[F[_, _], T, P](implicit
      converter: ValueConverter[T],
      refType: RefType[F],
      validate: Validate[T, P]
  ): ValueConverter[F[T, P]] = converter.map(t => refType.refine[P].unsafeFrom(t))

}
