package eu.timepit

import shapeless.tag
import shapeless.tag.@@

package object refined {
  def refine[P, T](t: T)(implicit p: Predicate[P, T]): Either[String, T @@ P] =
    if (p.isValid(t)) Right(tag[P](t))
    else Left(p.show(t))

  def refineUnsafe[P, T](t: T)(implicit p: Predicate[P, T]): T @@ P =
    if (p.isValid(t)) tag[P](t)
    else throw new IllegalArgumentException(p.show(t))
}
