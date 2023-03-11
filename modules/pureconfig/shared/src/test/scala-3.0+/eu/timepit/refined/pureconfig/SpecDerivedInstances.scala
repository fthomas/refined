package eu.timepit.refined.pureconfig

import eu.timepit.refined.types.numeric.PosInt
import _root_.pureconfig.{ConfigReader, ConfigWriter}
import _root_.pureconfig.generic.derivation.default.derived
import com.typesafe.config.ConfigValueFactory

import scala.jdk.CollectionConverters.*

trait SpecDerivedInstances {

  given ConfigReader[Config] = ConfigReader.derived
  // we only care that this part of config writer (refined type) compiles
  private val posIntConfWriter: ConfigWriter[PosInt] = summon
  // there's no builtin generic derivation of ConfigWriter[ADT] for scala 3 yet
  given ConfigWriter[Config] = posIntConfWriter
    .contramap[Config](_.value)
    .mapConfig(cfg => ConfigValueFactory.fromMap(Map("value" -> cfg).asJava))
}
