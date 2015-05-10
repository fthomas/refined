enablePlugins(GitVersioning)

name := "refined"

organization := "eu.timepit"
startYear := Some(2015)
licenses += "GPL-3.0" -> url("http://www.gnu.org/licenses/gpl-3.0.html")

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

scalacOptions in (Compile, doc) ++= Seq(
  "-diagrams",
  "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
  "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
)

autoAPIMappings := true

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.2.0-RC6",
  "org.scalacheck" %% "scalacheck" % "1.12.2" % "test"
)

scmInfo := Some(ScmInfo(url("https://github.com/fthomas/refined"),
  "git@github.com:fthomas/refined.git"))

initialCommands := """
  import refined._
  import refined.boolean._
  import refined.numeric._
  import refined.string._
  import shapeless.nat._
  import shapeless.tag.@@
"""

bintraySettings

publishMavenStyle := true

scalariformSettings

git.useGitDescribe := true
