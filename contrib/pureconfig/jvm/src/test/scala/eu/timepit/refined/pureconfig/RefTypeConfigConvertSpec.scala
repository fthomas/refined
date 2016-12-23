package eu.timepit.refined.pureconfig

import com.typesafe.config.ConfigFactory
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.pureconfig.error.PredicateFailedException
import org.scalacheck.Prop._
import org.scalacheck.Properties
import pureconfig._
import scala.util.{Failure, Success, Try}

class RefTypeConfigConvertSpec extends Properties("RefTypeConfigConvert") {

  type PosInt = Int Refined Positive
  case class Config(value: PosInt)

  property("load success") = secure {
    loadConfigWithValue("1") ?=
      Success(Config(1))
  }

  property("load failure") = secure {
    loadConfigWithValue("0") =?
      Failure(PredicateFailedException("Predicate failed: (0 > 0)."))
  }

  def loadConfigWithValue(value: String): Try[Config] =
    loadConfig[Config](ConfigFactory.parseString(s"value = $value"))
}
