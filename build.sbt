lazy val root = project.in(file("."))
  .aggregate(
    coreJVM,
    coreJS,
    docs,
    scalacheckJVM,
    scalacheckJS)
  .settings(commonSettings)
  .settings(noPublishSettings)
  .settings(releaseSettings)
  .settings(
    console <<= console in (coreJVM, Compile),
    parallelExecution in Test in ThisBuild := false
  )

lazy val core = crossProject
  .enablePlugins(BuildInfoPlugin)
  .settings(moduleName := "refined")
  .settings(commonSettings: _*)
  .settings(scaladocSettings: _*)
  .settings(publishSettings: _*)
  .settings(miscSettings: _*)
  .settings(releaseSettings: _*)
  .settings(styleSettings: _*)
  .settings(siteSettings: _*)
  .jvmSettings(myDoctestSettings: _*)
  .jsSettings(scalaJSStage in Test := FastOptStage)

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

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
  .dependsOn(coreJVM)

lazy val scalacheck = crossProject
  .settings(moduleName := "refined-scalacheck")
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(releaseSettings: _*)
  .settings(styleSettings: _*)
  .settings(libraryDependencies += "org.scalacheck" %%% "scalacheck" % scalaCheckVersion)
  .jsSettings(scalaJSStage in Test := FastOptStage)
  .dependsOn(core)

lazy val scalacheckJVM = scalacheck.jvm
lazy val scalacheckJS = scalacheck.js

val rootPkg = "eu.timepit.refined"
val gitPubUrl = "https://github.com/fthomas/refined.git"
val gitDevUrl = "git@github.com:fthomas/refined.git"

lazy val shapelessVersion = "2.2.5"
lazy val scalaCheckVersion = "1.12.5"

lazy val commonSettings =
  projectSettings ++
  compileSettings

lazy val projectSettings = Seq(
  name := "refined",
  description := "Simple refinement types for Scala",

  organization := "eu.timepit",
  homepage := Some(url("https://github.com/fthomas/refined")),
  startYear := Some(2015),
  licenses += "MIT" -> url("http://opensource.org/licenses/MIT"),

  scmInfo := Some(ScmInfo(homepage.value.get,
    s"scm:git:$gitPubUrl", Some(s"scm:git:$gitDevUrl")))
)

lazy val compileSettings = Seq(
  scalaVersion := "2.11.7",
  crossScalaVersions := Seq("2.11.7", "2.10.6"),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:experimental.macros",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    //"-Xfatal-warnings",
    "-Xfuture",
    "-Xlint",
    //"-Xlog-implicits",
    //"-Xprint:posterasure",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard"
  ),

  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-compiler" % scalaVersion.value,
    "com.chuusai" %%% "shapeless" % shapelessVersion,
    "org.scalacheck" %%% "scalacheck" % scalaCheckVersion % "test"
  ),

  libraryDependencies ++= {
    if (scalaVersion.value startsWith "2.10.")
      // this is required for shapeless.LabelledGeneric
      Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full))
    else
      Seq.empty
  },

  wartremoverErrors in (Compile, compile) ++= Warts.unsafe diff Seq(
    Wart.Any,
    Wart.DefaultArguments,
    Wart.AsInstanceOf,
    Wart.NonUnitStatements,
    Wart.Null,
    Wart.Throw
  )
)

lazy val scaladocSettings = Seq(
  scalacOptions in (Compile, doc) ++= Seq(
    //"-diagrams",
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

  lazy val addReleaseDateToReleaseNotes: ReleaseStep = { st: State =>
    val extracted = Project.extract(st)
    val newVersion = extracted.get(version)
    val date = "date +%Y-%m-%d".!!.trim
    val footer = s"\nReleased on $date\n"

    val notes = s"notes/$newVersion.markdown"
    IO.append(file(notes), footer)
    s"git add $notes" !! st.log

    st
  }

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
    releaseCrossBuild := true,
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      addReleaseDateToReleaseNotes,
      updateVersionInReadme,
      commitReleaseVersion,
      tagRelease,
      publishArtifacts,
      releaseStepTask(bintraySyncMavenCentral),
      releaseStepTask(GhPagesKeys.pushSite in "coreJVM"),
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
  initialCommands := s"""
    import $rootPkg._
    import $rootPkg.api._
    import $rootPkg.api.Inference.==>
    import $rootPkg.api.RefType.ops._
    import $rootPkg.auto._
    import $rootPkg.boolean._
    import $rootPkg.char._
    import $rootPkg.collection._
    import $rootPkg.generic._
    import $rootPkg.numeric._
    import $rootPkg.string._
    import shapeless.{ ::, HList, HNil }
    import shapeless.nat._
    import shapeless.tag.@@
  """,

  buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
  buildInfoPackage := s"$rootPkg.internal"
)

lazy val myDoctestSettings =
  doctestSettings ++ Seq(doctestWithDependencies := false)

lazy val styleSettings =
  scalariformSettings ++
  Seq(
    sourceDirectories in (Compile, SbtScalariform.ScalariformKeys.format) :=
      (sourceDirectories in Compile).value,
    sourceDirectories in (Test, SbtScalariform.ScalariformKeys.format) :=
      (sourceDirectories in Test).value
  )

addCommandAlias("validate", Seq(
  "clean",
  "coreJS/test",
  "scalacheckJS/test",
  "coverage",
  "compile",
  "coreJVM/test",
  "scalacheckJVM/test",
  "scalastyle",
  "test:scalastyle",
  "doc",
  "docs/tut"
).mkString(";", ";", ""))
