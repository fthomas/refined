package eu.timepit.refined.pureconfig

import _root_.pureconfig.{ConfigCursor, ConfigReader, ConfigWriter}
import _root_.pureconfig.error.{CannotConvert, ConvertFailure, ConfigReaderFailures}
import eu.timepit.refined.api.{RefType, Validate}
import com.typesafe.config.ConfigValue

trait BaseInstances extends TypeDescribeVersionSpecific {

  implicit def refTypeConfigReader[F[_, _], T, P](implicit
      configReader: ConfigReader[T],
      refType: RefType[F],
      validate: Validate[T, P],
      describeType: DescribeType[F[T, P]]
  ): ConfigReader[F[T, P]] = new ConfigReader[F[T, P]] {

    override def from(cur: ConfigCursor): Either[ConfigReaderFailures, F[T, P]] =
      configReader.from(cur) match {
        case Right(t) =>
          refType.refine[P](t) match {
            case Left(because) =>
              Left(
                ConfigReaderFailures(
                  ConvertFailure(
                    reason = CannotConvert(
                      value = cur.valueOpt.map(_.render()).getOrElse("none"),
                      toType = getDescription(describeType),
                      because = because
                    ),
                    cur = cur
                  )
                )
              )

            case Right(refined) =>
              Right(refined)
          }

        case Left(configReaderFailures) =>
          Left(configReaderFailures)
      }
  }

  implicit def refTypeConfigWriter[F[_, _], T, P](implicit
      configWriter: ConfigWriter[T],
      refType: RefType[F]
  ): ConfigWriter[F[T, P]] =
    new ConfigWriter[F[T, P]] {
      override def to(a: F[T, P]): ConfigValue = configWriter.to(refType.unwrap(a))
    }

}
