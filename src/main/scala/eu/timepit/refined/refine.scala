package eu.timepit.refined

import shapeless.tag
import shapeless.tag.@@

import scala.language.experimental.macros

object refine {
  def apply[P] = new Refinery[P]

  class Refinery[P] {
    def apply[T](t: T)(implicit p: Predicate[P, T]): Either[String, T @@ P] =
      p.validated(t) match {
        case Some(s) => Left(s)
        case None => Right(tag[P](t))
      }
  }
}

object refineLit {
  def apply[P] = new Refinery[P]

  class Refinery[P] {
    def apply[T](t: T)(implicit p: Predicate[P, T]): T @@ P = macro internal.refineLitImpl[P, T]
  }
}
