enablePlugins(GitVersioning)

name := "refined"

organization := "eu.timepit"
homepage := Some(url("https://github.com/fthomas/refined"))
startYear := Some(2015)
licenses += "MIT" -> url("http://opensource.org/licenses/MIT")

val gitRepo = "git@github.com:fthomas/refined.git"
scmInfo := Some(ScmInfo(homepage.value.get,
  "scm:git:https://github.com/fthomas/refined.git",
  Some(s"scm:git:$gitRepo")))

scalaVersion := "2.11.6"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "com.chuusai" %% "shapeless" % "2.2.0-RC6",
  "org.scalacheck" %% "scalacheck" % "1.12.3" % "test"
)

initialCommands := """
  import eu.timepit.refined._
  import eu.timepit.refined.boolean._
  import eu.timepit.refined.char._
  import eu.timepit.refined.collection._
  import eu.timepit.refined.generic._
  import eu.timepit.refined.numeric._
  import eu.timepit.refined.string._
  import shapeless.{ ::, HList, HNil }
  import shapeless.nat._
  import shapeless.tag.@@
"""

scalariformSettings

git.useGitDescribe := true

wartremoverErrors in (Compile, compile) ++= Warts.unsafe diff
  Seq(Wart.Any, Wart.AsInstanceOf, Wart.NonUnitStatements, Wart.Throw)

addCommandAlias("validate", ";clean;coverage;test;scalastyle;test:scalastyle;doc")

// doc settings

scalacOptions in (Compile, doc) ++= Seq(
  "-diagrams",
  "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
  "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
)

autoAPIMappings := true
apiURL := Some(url("http://fthomas.github.io/refined/latest/api/"))

// publish settings

publishMavenStyle := true
pomIncludeRepository := { _ => false }
pomExtra :=
  <developers>
    <developer>
      <id>fthomas</id>
      <name>Frank S. Thomas</name>
      <url>https://github.com/fthomas</url>
    </developer>
  </developers>

// site settings

site.settings
site.includeScaladoc()
ghpages.settings
git.remoteRepo := gitRepo

// release settings
import ReleaseTransformations._
releasePublishArtifactsAction := PgpKeys.publishSigned.value
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  releaseStepTask(GhPagesKeys.pushSite),
  setNextVersion,
  commitNextVersion,
  pushChanges
)
