import sbt.Keys.version
import sbt._
import sbtrelease.ReleasePlugin.autoImport.ReleaseStep
import sbtrelease.ReleaseStateTransformations.reapply

object LatestVersion extends AutoPlugin {
  object autoImport {
    lazy val latestVersion: SettingKey[String] =
      settingKey[String]("latest released version")

    lazy val setLatestVersion: ReleaseStep = { st: State =>
      val extracted = Project.extract(st)
      val newVersion = extracted.get(version)

      val latestVersionSbt = "latestVersion.sbt"
      val content = Seq(s"""latestVersion in ThisBuild := "$newVersion"""")

      IO.writeLines(file(latestVersionSbt), content)
      val vcs = sbtrelease.Vcs.detect(file("."))
      vcs.foreach(_.add(latestVersionSbt) !! st.log)

      reapply(Seq(latestVersion in ThisBuild := newVersion), st)
    }
  }
}
