package eu.timepit

import shapeless.tag
import shapeless.tag.@@

package object refined {
  def refine[P, X](x: X)(implicit p: Predicate[P, X]): Either[String, X @@ P] =
    p.validate(x) match {
      case Some(s) => Left(s)
      case None => Right(tag[P](x))
    }

  def refineUnsafe[P, X](x: X)(implicit p: Predicate[P, X]): X @@ P =
    p.validate(x).fold(tag[P](x))(s => throw new IllegalArgumentException(s))
}
