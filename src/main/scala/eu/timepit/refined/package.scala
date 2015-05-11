package eu.timepit

import shapeless.tag
import shapeless.tag.@@

package object refined {
  def refine[P, T](t: T)(implicit p: Predicate[P, T]): Either[String, T @@ P] =
    p.validated(t) match {
      case Some(s) => Left(s)
      case None => Right(tag[P](t))
    }

  def refineUnsafe[P, T](t: T)(implicit p: Predicate[P, T]): T @@ P =
    p.validated(t).fold(tag[P](t))(s => throw new IllegalArgumentException(s))
}
