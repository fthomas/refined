package eu.timepit.refined.pureconfig

import _root_.pureconfig.{ConfigReader, ConfigWriter}
import com.typesafe.config.ConfigValueFactory
import _root_.pureconfig.generic.derivation.default.derived
import eu.timepit.refined.types.numeric.PosInt

import java.util.{Map as JMap}

trait SpecDerivedInstances {

  given ConfigReader[Config] = ConfigReader.derived
  // we only care that this part of config writer (refined type) compiles
  private val posIntConfWriter: ConfigWriter[PosInt] = summon
  // there's no builtin generic derivation of ConfigWriter[ADT] for scala 3 yet
  given ConfigWriter[Config] = posIntConfWriter.contramap[Config](_.value).mapConfig(cfg =>
    ConfigValueFactory.fromMap(JMap.of("value", cfg))
  )
}
