import sbt._
import sbt.Keys.version
import sbtrelease.ReleasePlugin.autoImport.ReleaseStep
import sbtrelease.ReleaseStateTransformations.reapply
import scala.sys.process.ProcessLogger

object LatestVersion extends AutoPlugin {
  object autoImport {

    lazy val latestVersion: SettingKey[String] =
      settingKey[String]("latest released version")

    lazy val latestVersionInSeries: SettingKey[Option[String]] =
      settingKey[Option[String]]("latest released version in this binary-compatible series")

    lazy val unreleasedModules: SettingKey[Set[String]] =
      settingKey[Set[String]]("the names of modules which are new in the upcoming release")

    lazy val setLatestVersion: ReleaseStep = { state: State =>
      val extracted = Project.extract(state)
      val newVersion = extracted.get(version)

      val latestVersionSbt = "latestVersion.sbt"
      val content = Seq(
        s"""|latestVersion in ThisBuild := "$newVersion"
            |
            |latestVersionInSeries in ThisBuild := Some("$newVersion")
            |
            |unreleasedModules in ThisBuild := Set(
            |  // Example:
            |  // "refined-eval"
            |)
            |""".stripMargin.trim
      )

      IO.writeLines(file(latestVersionSbt), content)
      val vcs = sbtrelease.Vcs.detect(file("."))
      vcs.foreach(_.add(latestVersionSbt) !! toProcessLogger(state.log))

      reapply(
        Seq(
          latestVersion in ThisBuild := newVersion,
          latestVersionInSeries in ThisBuild := Some(newVersion)
        ),
        state
      )
    }
  }

  def toProcessLogger(log: Logger): ProcessLogger =
    new ProcessLogger {
      override def out(s: => String): Unit = log.info(s)
      override def err(s: => String): Unit = log.info(s)
      override def buffer[T](f: => T): T = log.buffer(f)
    }
}
