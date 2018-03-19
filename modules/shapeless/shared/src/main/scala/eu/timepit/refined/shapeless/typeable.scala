package eu.timepit.refined.shapeless

import _root_.shapeless.Typeable
import eu.timepit.refined.api.{RefType, Validate}
import scala.util.Right

package object typeable {

  /**
   * `Typeable` instance for refined types.
   */
  implicit def refTypeTypeable[F[_, _], T, P](implicit rt: RefType[F],
                                              V: Validate[T, P],
                                              T: Typeable[T],
                                              P: Typeable[P]): Typeable[F[T, P]] =
    new Typeable[F[T, P]] {
      override def cast(t: Any): Option[F[T, P]] =
        T.cast(t)
          .flatMap(casted =>
            rt.refine[P](casted) match {
              case Right(v) => Some(v)
              case _        => None
          })
      override def describe: String = s"Refined[${T.describe}, ${P.describe}]"
    }
}
