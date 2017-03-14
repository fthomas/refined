package eu.timepit.refined.pureconfig

import com.typesafe.config.ConfigFactory
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import pureconfig._
import pureconfig.error.{CannotConvert, ConfigReaderFailures}

class RefTypeConfigConvertSpec extends Properties("RefTypeConfigConvert") {

  type PosInt = Int Refined Positive
  case class Config(value: PosInt)

  property("load success") = secure {
    loadConfigWithValue("1") ?=
      Right(Config(1))
  }

  property("load failure (predicate)") = secure {
    loadConfigWithValue("0") =?
      Left(
        ConfigReaderFailures(CannotConvert(
          value = "0",
          toTyp = "eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Greater[shapeless.nat._0]]",
          because = "Predicate failed: (0 > 0).",
          location = None
        )))
  }

  property("load failure (wrong type)") = secure {
    loadConfigWithValue("abc") =?
      Left(
        ConfigReaderFailures(
          CannotConvert(
            value = "abc",
            toTyp = "Int",
            because = "java.lang.NumberFormatException: For input string: \"abc\"",
            location = None
          )))
  }

  property("roundtrip success") = secure {
    val config = Config(1)
    val configValue = ConfigConvert[Config].to(config)
    ConfigConvert[Config].from(configValue) ?=
      Right(config)
  }

  def loadConfigWithValue(value: String): Either[ConfigReaderFailures, Config] =
    loadConfig[Config](ConfigFactory.parseString(s"value = $value"))
}
