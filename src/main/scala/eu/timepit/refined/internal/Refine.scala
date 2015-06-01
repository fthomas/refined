package eu.timepit.refined
package internal

import shapeless.tag.@@

final class Refine[P] {
  def apply[T](t: T)(implicit p: Predicate[P, T]): Either[String, T @@ P] =
    p.validated(t) match {
      case Some(s) => Left(s)
      case None => Right(t.asInstanceOf[T @@ P])
    }
}
