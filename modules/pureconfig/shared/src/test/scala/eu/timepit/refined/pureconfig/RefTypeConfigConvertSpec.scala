package eu.timepit.refined.pureconfig

import com.typesafe.config.{ConfigOriginFactory, ConfigValueType}
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties
import pureconfig._
import pureconfig.error.{CannotConvert, ConfigReaderFailures, ConvertFailure, WrongType}

class RefTypeConfigConvertSpec
    extends Properties("RefTypeConfigConvert")
    with SpecDerivedInstances {

  property("load success") = secure {
    loadConfigWithValue("1") ?=
      Right(Config(PosInt.unsafeFrom(1)))
  }

  property("load failure (predicate)") = secure {

    def expectedFailure(toTypeMsg: String) = Left(
      ConfigReaderFailures(
        ConvertFailure(
          reason = CannotConvert(
            value = "0",
            toType = toTypeMsg,
            because = "Predicate failed: (0 > 0)."
          ),
          origin = Some(ConfigOriginFactory.newSimple("String").withLineNumber(1)),
          path = "value"
        )
      )
    )

    val expected1 = expectedFailure(
      "eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Positive]"
    )

    // Allow "scala.Int" instead of just "Int" in the toType parameter.
    // For some reason Scala 2.12 with sbt 1.1.2 uses the former.
    val expected2 = expectedFailure(
      "eu.timepit.refined.api.Refined[scala.Int,eu.timepit.refined.numeric.Positive]"
    )

    // scala 3 macro provide type representation in this way
    val expected3 = expectedFailure(
      "eu.timepit.refined.api.Refined[scala.Int, eu.timepit.refined.numeric.Greater[shapeless.nat._0]]"
    )

    val actual = loadConfigWithValue("0")
    (actual ?= expected1) ||
    (actual ?= expected2) ||
    (actual ?= expected3)
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
