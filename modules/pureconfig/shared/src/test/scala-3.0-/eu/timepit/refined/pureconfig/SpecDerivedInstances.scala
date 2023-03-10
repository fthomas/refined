package eu.timepit.refined.pureconfig

import _root_.pureconfig.{ConfigReader, ConfigWriter}
import _root_.pureconfig.generic.auto

trait SpecDerivedInstances {

  implicit val configReader: ConfigReader[Config] = auto.exportReader.instance
  implicit val configWriter: ConfigWriter[Config] = auto.exportWriter.instance
}
