package eu.timepit.refined.examples

import eu.timepit.refined.api.RefType.ops._
import eu.timepit.refined.api.{ RefType, Validate }

class RefTypeExperiments[P, R[_, _]](implicit rt: RefType[R]) {

  type F[x] = R[x, P]
  type G[x] = Either[String, x]

  // Monad-like functions

  def pureLike[A](a: A)(implicit v: Validate[A, P]): G[F[A]] =
    rt.refine[P](a)

  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] =
    f(extract(fa))

  // Comonad-like functions

  def extract[A](fa: F[A]): A =
    fa.unwrap

  def coflatMapLike[A, B](fa: F[A])(f: F[A] => B)(implicit v: Validate[B, P]): G[F[B]] =
    pureLike(f(fa))

  // Notice the antisymmetry in:
  // pureLike <-> extract
  // flatMap  <-> coflatMapLike

  // Let's define mapLike:

  def mapLikeViaCoflatMapAndExtract[A, B](fa: F[A])(f: A => B)(implicit v: Validate[B, P]): G[F[B]] =
    coflatMapLike(fa)(fa => f(extract(fa)))

  //def mapLikeViaFlatMapAndPure[A, B](fa: F[A])(f: A => B): G[F[B]] =
  //  flatMap(fa)(a => pureLike(f(a)))
  //
  // We can't define mapLike with the Monad-like functions because the
  // lifting into G[F[_]] can only happen inside flatMap but flatMap
  // does not allow an additional G[_] layer.
}
