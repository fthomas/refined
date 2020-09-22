import sbt._

object LatestVersion extends AutoPlugin {
  object autoImport {

    lazy val latestVersion: SettingKey[String] =
      settingKey[String]("latest released version")

    lazy val bincompatVersions: SettingKey[Map[String, Set[String]]] =
      settingKey[Map[String, Set[String]]](
        "set of versions that are checked for binary compatibility with the current HEAD"
      )

    lazy val unreleasedModules: SettingKey[Set[String]] =
      settingKey[Set[String]]("the names of modules which are new in the upcoming release")
  }
}
