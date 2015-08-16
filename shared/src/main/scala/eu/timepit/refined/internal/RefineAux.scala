package eu.timepit.refined
package internal

/**
 * Helper class that allows the type `T` to be inferred from calls like
 * `[[RefType.refine]][P](t)`.
 *
 * See [[http://tpolecat.github.io/2015/07/30/infer.html]] for a detailed
 * explanation of this trick.
 */
final class RefineAux[F[_, _], P](rt: RefType[F]) {

  def apply[T](t: T)(implicit p: Predicate[P, T]): Either[String, F[T, P]] =
    p.validate(t) match {
      case None => Right(rt.unsafeWrap(t))
      case Some(s) => Left(s)
    }
}
