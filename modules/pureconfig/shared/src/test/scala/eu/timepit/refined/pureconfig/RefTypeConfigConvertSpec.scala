package eu.timepit.refined.pureconfig

import com.typesafe.config.{ConfigOriginFactory, ConfigValueType}
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties
import pureconfig._
import pureconfig.error.{CannotConvert, ConfigReaderFailures, ConvertFailure, WrongType}
import pureconfig.generic.auto._

class RefTypeConfigConvertSpec extends Properties("RefTypeConfigConvert") {

  case class Config(value: PosInt)

  property("load success") = secure {
    loadConfigWithValue("1") ?=
      Right(Config(PosInt.unsafeFrom(1)))
  }

  property("load failure (predicate)") = secure {
    val expected1 = Left(
      ConfigReaderFailures(
        ConvertFailure(
          reason = CannotConvert(
            value = "0",
            toType = "eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Positive]",
            because = "Predicate failed: (0 > 0)."
          ),
          origin = Some(ConfigOriginFactory.newSimple("String").withLineNumber(1)),
          path = "value"
        )
      )
    )

    // Allow "scala.Int" instead of just "Int" in the toType parameter.
    // For some reason Scala 2.12 with sbt 1.1.2 uses the former.
    val expected2 = Left(
      ConfigReaderFailures(
        ConvertFailure(
          reason = CannotConvert(
            value = "0",
            toType =
              "eu.timepit.refined.api.Refined[scala.Int,eu.timepit.refined.numeric.Positive]",
            because = "Predicate failed: (0 > 0)."
          ),
          origin = Some(ConfigOriginFactory.newSimple("String").withLineNumber(1)),
          path = "value"
        )
      )
    )

    val actual = loadConfigWithValue("0")
    (actual ?= expected1) ||
    (actual ?= expected2)
  }

  property("load failure (wrong type)") = secure {
    loadConfigWithValue("abc") =?
      Left(
        ConfigReaderFailures(
          ConvertFailure(
            reason = WrongType(
              foundType = ConfigValueType.STRING,
              expectedTypes = Set(ConfigValueType.NUMBER)
            ),
            origin = Some(ConfigOriginFactory.newSimple("String").withLineNumber(1)),
            path = "value"
          )
        )
      )
  }

  property("roundtrip success") = secure {
    val config = Config(PosInt.unsafeFrom(1))
    val configValue = ConfigConvert[Config].to(config)
    ConfigConvert[Config].from(configValue) ?=
      Right(config)
  }

  def loadConfigWithValue(value: String): Either[ConfigReaderFailures, Config] =
    ConfigSource.string(s"value = $value").load[Config]
}
