package eu.timepit

import shapeless.tag
import shapeless.tag.@@

import scala.language.experimental.macros

package object refined {
  def refine[P, T](t: T)(implicit p: Predicate[P, T]): Either[String, T @@ P] =
    p.validated(t) match {
      case Some(s) => Left(s)
      case None => Right(tag[P](t))
    }

  def refineLit[P, T](t: T)(implicit p: Predicate[P, T]): T @@ P = macro internal.refineLitImpl[P, T]
}
