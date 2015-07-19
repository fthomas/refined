lazy val root = project.in(file("."))
  .aggregate(refinedJVM, refinedJS, docs)
  .settings(commonSettings)
  .settings(noPublishSettings)
  .settings(releaseSettings)
  .settings(styleSettings)
  .settings(
    console <<= console in (refinedJVM, Compile),
    parallelExecution in ThisBuild := false
  )

lazy val refined = crossProject.in(file("."))
  .settings(commonSettings: _*)
  .settings(scaladocSettings: _*)
  .settings(publishSettings: _*)
  .settings(miscSettings: _*)
  .settings(releaseSettings: _*)
  .settings(styleSettings: _*)
  .settings(siteSettings: _*)
  .jvmSettings()
  .jsSettings(scalaJSStage in Test := FastOptStage)

lazy val refinedJVM = refined.jvm
lazy val refinedJS = refined.js

lazy val docs = project
  .settings(moduleName := "refined-docs")
  .settings(commonSettings)
  .settings(noPublishSettings)
  .settings(tutSettings)
  .settings(
    tutScalacOptions := scalacOptions.value,
    tutSourceDirectory := baseDirectory.value / "src",
    tutTargetDirectory := baseDirectory.value
  )
  .dependsOn(refinedJVM)

val gitPubUrl = "https://github.com/fthomas/refined.git"
val gitDevUrl = "git@github.com:fthomas/refined.git"

lazy val commonSettings =
  projectSettings ++
  compileSettings

lazy val projectSettings = Seq(
  name := "refined",
  description := "Refinement types for Scala",

  organization := "eu.timepit",
  homepage := Some(url("https://github.com/fthomas/refined")),
  startYear := Some(2015),
  licenses += "MIT" -> url("http://opensource.org/licenses/MIT"),

  scmInfo := Some(ScmInfo(homepage.value.get,
    s"scm:git:$gitPubUrl", Some(s"scm:git:$gitDevUrl")))
)

lazy val compileSettings = Seq(
  scalaVersion := "2.11.7",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:experimental.macros",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xfuture",
    "-Xlint",
    //"-Xlog-implicits",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard"
  ),

  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-compiler" % scalaVersion.value,
    "com.chuusai" %%% "shapeless" % "2.2.4",
    "org.scalacheck" %%% "scalacheck" % "1.12.4" % "test"
  ),

  wartremoverErrors in (Compile, compile) ++= Warts.unsafe diff
    Seq(Wart.Any, Wart.DefaultArguments, Wart.AsInstanceOf, Wart.NonUnitStatements, Wart.Null, Wart.Throw)
)

lazy val scaladocSettings = Seq(
  scalacOptions in (Compile, doc) ++= Seq(
    "-diagrams",
    "-diagrams-debug",
    "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
    "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
  ),

  autoAPIMappings := true,
  apiURL := Some(url("http://fthomas.github.io/refined/latest/api/"))
)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  pomIncludeRepository := { _ => false },
  pomExtra :=
    <developers>
      <developer>
        <id>fthomas</id>
        <name>Frank S. Thomas</name>
        <url>https://github.com/fthomas</url>
      </developer>
    </developers>
)

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

lazy val releaseSettings = {
  import ReleaseTransformations._

  lazy val updateVersionInReadme: ReleaseStep = { st: State =>
    val extracted = Project.extract(st)
    val newVersion = extracted.get(version)
    val oldVersion = "git describe --abbrev=0".!!.trim.replaceAll("^v", "")

    val readme = "README.md"
    val oldContent = IO.read(file(readme))
    val newContent = oldContent.replaceAll(oldVersion, newVersion)
    IO.write(file(readme), newContent)
    s"git add $readme" !! st.log

    st
  }

  Seq(
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      updateVersionInReadme,
      commitReleaseVersion,
      tagRelease,
      publishArtifacts,
      releaseStepTask(GhPagesKeys.pushSite in "refinedJVM"),
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )
}

lazy val siteSettings =
  site.settings ++
  site.includeScaladoc() ++
  ghpages.settings ++
  Seq(git.remoteRepo := gitDevUrl)

lazy val miscSettings = Seq(
  initialCommands := """
    import eu.timepit.refined._
    import eu.timepit.refined.boolean._
    import eu.timepit.refined.char._
    import eu.timepit.refined.collection._
    import eu.timepit.refined.generic._
    import eu.timepit.refined.InferenceRule.==>
    import eu.timepit.refined.implicits._
    import eu.timepit.refined.numeric._
    import eu.timepit.refined.string._
    import shapeless.{ ::, HList, HNil }
    import shapeless.nat._
    import shapeless.tag.@@
  """,

  doctestWithDependencies := false
) // ++ doctestSettings
// Enable again when https://github.com/tkawachi/sbt-doctest/issues/52 is fixed.

lazy val styleSettings =
  scalariformSettings ++
  Seq(
    sourceDirectories in (Compile, ScalariformKeys.format) +=
      baseDirectory.value / "shared/src/main/scala",
    sourceDirectories in (Test, ScalariformKeys.format) +=
      baseDirectory.value / "shared/src/test/scala"
  )

addCommandAlias("validate", Seq(
  "clean",
  "refinedJS/test",
  "coverage",
  "compile",
  "refinedJVM/test",
  "scalastyle",
  "test:scalastyle",
  "doc",
  "docs/tut"
).mkString(";", ";", ""))
