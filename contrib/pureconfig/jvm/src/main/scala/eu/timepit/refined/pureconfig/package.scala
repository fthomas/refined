package eu.timepit.refined

import _root_.pureconfig.ConfigConvert
import com.typesafe.config.ConfigValue
import eu.timepit.refined.api.{RefType, Validate}
import eu.timepit.refined.pureconfig.error.PredicateFailedException
import scala.util.{Failure, Success, Try}

package object pureconfig {

  implicit def refTypeConfigConvert[F[_, _], T, P](
      implicit configConvert: ConfigConvert[T],
      refType: RefType[F],
      validate: Validate[T, P]
  ): ConfigConvert[F[T, P]] = new ConfigConvert[F[T, P]] {
    override def from(config: ConfigValue): Try[F[T, P]] =
      configConvert.from(config).flatMap { t ⇒
        refType.refine[P](t).left.map(PredicateFailedException) match {
          case Right(refined) ⇒ Success(refined)
          case Left(exception) ⇒ Failure(exception)
        }
      }

    override def to(t: F[T, P]): ConfigValue =
      configConvert.to(refType.unwrap(t))
  }
}
