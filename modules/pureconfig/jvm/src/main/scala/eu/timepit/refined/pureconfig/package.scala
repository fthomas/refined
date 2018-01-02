package eu.timepit.refined

import _root_.pureconfig.ConfigConvert
import _root_.pureconfig.error.{CannotConvert, ConfigReaderFailures, ConfigValueLocation}
import com.typesafe.config.ConfigValue
import eu.timepit.refined.api.{RefType, Validate}
import scala.reflect.runtime.universe.WeakTypeTag

package object pureconfig {

  implicit def refTypeConfigConvert[F[_, _], T, P](
      implicit configConvert: ConfigConvert[T],
      refType: RefType[F],
      validate: Validate[T, P],
      typeTag: WeakTypeTag[F[T, P]]
  ): ConfigConvert[F[T, P]] = new ConfigConvert[F[T, P]] {
    override def from(config: ConfigValue): Either[ConfigReaderFailures, F[T, P]] =
      configConvert.from(config) match {
        case Right(t) =>
          refType.refine[P](t) match {
            case Left(because) =>
              Left(
                ConfigReaderFailures(
                  CannotConvert(
                    value = config.render(),
                    toType = typeTag.tpe.toString,
                    because = because,
                    location = ConfigValueLocation(config),
                    path = ""
                  )))

            case Right(refined) =>
              Right(refined)
          }

        case Left(configReaderFailures) =>
          Left(configReaderFailures)
      }

    override def to(t: F[T, P]): ConfigValue =
      configConvert.to(refType.unwrap(t))
  }
}
