package eu.timepit.refined
package internal

/**
 * Helper class that allows the type `T` to be inferred from calls like
 * `[[refineV]][P](t)`.
 */
final class Refine[P, F[_, _]](implicit r: Wrapper[F]) {

  def apply[T](t: T)(implicit p: Predicate[P, T]): Either[String, F[T, P]] =
    p.validate(t) match {
      case None => Right(r.wrap(t))
      case Some(s) => Left(s)
    }
}
