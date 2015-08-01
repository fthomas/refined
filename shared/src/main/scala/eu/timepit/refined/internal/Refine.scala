package eu.timepit.refined
package internal

/**
 * Helper class that allows the type `T` to be inferred from calls like
 * `[[refineV]][P](t)`.
 */
final class Refine[P, F[_, _]] {

  def apply[T](t: T)(implicit p: Predicate[P, T], w: Wrapper[F]): Either[String, F[T, P]] =
    p.validate(t) match {
      case None => Right(w.wrap(t))
      case Some(s) => Left(s)
    }
}
