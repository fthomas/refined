package eu.timepit.refined

import shapeless.tag._

import scala.language.experimental.macros

final class Refine[P] {
  def apply[T](t: T)(implicit p: Predicate[P, T]): Either[String, T @@ P] =
    p.validated(t) match {
      case Some(s) => Left(s)
      case None => Right(t.asInstanceOf[T @@ P])
    }
}

final class RefineLit[P] {
  def apply[T](t: T)(implicit p: Predicate[P, T]): T @@ P = macro internal.refineLitImpl[P, T]
}
