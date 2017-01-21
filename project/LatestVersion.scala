import sbt.Keys.version
import sbt._
import sbtrelease.ReleasePlugin.autoImport.ReleaseStep
import sbtrelease.ReleaseStateTransformations.reapply

object LatestVersion extends AutoPlugin {
  object autoImport {

    lazy val latestVersion: SettingKey[String] =
      settingKey[String]("latest released version")

    lazy val latestVersionInSeries: SettingKey[Option[String]] =
      settingKey[Option[String]]("latest released version in this binary-compatible series")

    lazy val unreleasedModules: SettingKey[Set[String]] =
      settingKey[Set[String]]("the names of modules which are new in the upcoming release")

    lazy val setLatestVersion: ReleaseStep = { st: State =>
      val extracted = Project.extract(st)
      val newVersion = extracted.get(version)

      val latestVersionSbt = "latestVersion.sbt"
      val content = Seq(
        s"""
          |latestVersion in ThisBuild := "$newVersion"
          |
          |latestVersionInSeries in ThisBuild := Some("$newVersion")
          |
          |unreleasedModules in ThisBuild := Seq(
          |// Example:
          |// "refined-eval"
          |)
        """.stripMargin
      )

      IO.writeLines(file(latestVersionSbt), content)
      val vcs = sbtrelease.Vcs.detect(file("."))
      vcs.foreach(_.add(latestVersionSbt) !! st.log)

      reapply(
        Seq(
          latestVersion in ThisBuild := newVersion,
          latestVersionInSeries in ThisBuild := Some(newVersion)
        ),
        st
      )
    }
  }
}

