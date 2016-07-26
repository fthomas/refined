/// shared variables

val latestVersion = "0.5.0"
val groupId = "eu.timepit"
val projectName = "refined"
val rootPkg = s"$groupId.$projectName"
val gitPubUrl = s"https://github.com/fthomas/$projectName.git"
val gitDevUrl = s"git@github.com:fthomas/$projectName.git"

val commonImports = s"""
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
"""

val macroCompatVersion = "1.1.1"
val macroParadiseVersion = "2.1.0"
val shapelessVersion = "2.3.1"
val scalaCheckVersion = "1.13.2"
val scalazVersion = "7.2.4"
val scodecVersion = "1.10.2"

val allSubprojects = List("core", "scalacheck", "scalaz", "scodec")
val allSubprojectsJVM = allSubprojects.map(_ + "JVM")
val allSubprojectsJS = allSubprojects.map(_ + "JS")

/// project definitions

lazy val root = project.in(file("."))
  .aggregate(
    coreJVM,
    coreJS,
    docs,
    scalacheckJVM,
    scalacheckJS,
    scalazJVM,
    scalazJS,
    scodecJVM,
    scodecJS)
  .settings(commonSettings)
  .settings(noPublishSettings)
  .settings(releaseSettings)
  .settings(
    console <<= console in (coreJVM, Compile),
    parallelExecution in Test in ThisBuild := false
  )

lazy val core = crossProject
  .enablePlugins(BuildInfoPlugin)
  .settings(moduleName := projectName)
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(miscSettings: _*)
  .settings(releaseSettings: _*)
  .settings(styleSettings: _*)
  .settings(siteSettings: _*)
  .settings(myDoctestSettings: _*)
  .jvmSettings(
    initialCommands := s"""
      $commonImports
      import shapeless.tag.@@
    """
  )
  .jvmSettings(submoduleJvmSettings: _*)
  .jsSettings(submoduleJsSettings: _*)

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

lazy val docs = project
  .settings(moduleName := s"$projectName-docs")
  .settings(commonSettings)
  .settings(noPublishSettings)
  .settings(tutSettings)
  .settings(
    tutScalacOptions := scalacOptions.value,
    tutSourceDirectory := baseDirectory.value / "src",
    tutTargetDirectory := baseDirectory.value
  )
  .dependsOn(coreJVM)

lazy val scalacheck = crossProject.in(file("contrib/scalacheck"))
  .settings(moduleName := s"$projectName-scalacheck")
  .settings(submoduleSettings: _*)
  .settings(libraryDependencies += "org.scalacheck" %%% "scalacheck" % scalaCheckVersion)
  .jvmSettings(
    initialCommands := s"""
      $commonImports
      import org.scalacheck.Arbitrary
    """
  )
  .jvmSettings(submoduleJvmSettings: _*)
  .jsSettings(submoduleJsSettings: _*)
  .dependsOn(core)

lazy val scalacheckJVM = scalacheck.jvm
lazy val scalacheckJS = scalacheck.js

lazy val scalaz = crossProject.in(file("contrib/scalaz"))
  .settings(moduleName := s"$projectName-scalaz")
  .settings(submoduleSettings: _*)
  .settings(libraryDependencies += "org.scalaz" %%% "scalaz-core" % scalazVersion)
  .jvmSettings(
    initialCommands := s"""
      $commonImports
      import $rootPkg.scalaz._
      import $rootPkg.scalaz.auto._
      import _root_.scalaz.@@
    """
  )
  .jvmSettings(submoduleJvmSettings: _*)
  .jsSettings(submoduleJsSettings: _*)
  .dependsOn(core % "compile->compile;test->test")

lazy val scalazJVM = scalaz.jvm
lazy val scalazJS = scalaz.js

lazy val scodec = crossProject.in(file("contrib/scodec"))
  .settings(moduleName := s"$projectName-scodec")
  .settings(submoduleSettings: _*)
  .settings(libraryDependencies += "org.scodec" %%% "scodec-core" % scodecVersion)
  .jvmSettings(
    initialCommands := s"""
      $commonImports
    """
  )
  .jvmSettings(submoduleJvmSettings: _*)
  .jsSettings(submoduleJsSettings: _*)
  .dependsOn(core % "compile->compile;test->test")

lazy val scodecJVM = scodec.jvm
lazy val scodecJS = scodec.js

/// settings definitions

lazy val commonSettings =
  projectSettings ++
  compileSettings ++
  scaladocSettings

lazy val submoduleSettings =
  commonSettings ++
  publishSettings ++
  releaseSettings ++
  styleSettings

lazy val submoduleJvmSettings = Seq(
  mimaPreviousArtifacts := Set(groupId %% moduleName.value % latestVersion),
  mimaBinaryIssueFilters ++= {
    import com.typesafe.tools.mima.core._
    import com.typesafe.tools.mima.core.ProblemFilters._
    Seq()
  }
)

lazy val submoduleJsSettings = Seq(
  doctestGenTests := Seq.empty,
  scalaJSUseRhino in Global := false
)

lazy val projectSettings = Seq(
  name := projectName,
  description := "Simple refinement types for Scala",

  organization := groupId,
  homepage := Some(url(s"https://github.com/fthomas/$projectName")),
  startYear := Some(2015),
  licenses += "MIT" -> url("http://opensource.org/licenses/MIT"),

  scmInfo := Some(ScmInfo(homepage.value.get,
    s"scm:git:$gitPubUrl", Some(s"scm:git:$gitDevUrl")))
)

lazy val compileSettings = Seq(
  scalaVersion := "2.11.8",
  crossScalaVersions := Seq(scalaVersion.value, "2.10.6"),
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
    compilerPlugin("org.scalamacros" % "paradise" % macroParadiseVersion cross CrossVersion.full),
    "org.typelevel" %%% "macro-compat" % macroCompatVersion,
    "com.chuusai" %%% "shapeless" % shapelessVersion,
    "org.scalacheck" %%% "scalacheck" % scalaCheckVersion % "test"
  ),

  wartremoverErrors in (Compile, compile) ++= Warts.unsafe diff Seq(
    Wart.Any,
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
  apiURL := Some(url(s"http://fthomas.github.io/$projectName/latest/api/"))
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
      setReleaseVersion,
      addReleaseDateToReleaseNotes,
      updateVersionInReadme,
      commitReleaseVersion,
      tagRelease,
      publishArtifacts,
      releaseStepTask(GhPagesKeys.pushSite in "coreJVM"),
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )
}

addCommandAlias("syncMavenCentral",
  allSubprojectsJVM.map(_ + "/bintraySyncMavenCentral").mkString(";", ";", ""))

lazy val siteSettings = Def.settings(
  site.settings,
  site.includeScaladoc(),
  ghpages.settings,
  git.remoteRepo := gitDevUrl
)

lazy val miscSettings = Seq(
  buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
  buildInfoPackage := s"$rootPkg.internal"
)

lazy val myDoctestSettings = Def.settings(
  doctestWithDependencies := false
)

lazy val styleSettings = Def.settings(
  scalariformSettings,
  sourceDirectories in (Compile, SbtScalariform.ScalariformKeys.format) :=
    (sourceDirectories in Compile).value,
  sourceDirectories in (Test, SbtScalariform.ScalariformKeys.format) :=
    (sourceDirectories in Test).value,

  // workaround for https://github.com/scalastyle/scalastyle-sbt-plugin/issues/47
  scalastyleSources in Compile :=
    (unmanagedSourceDirectories in Compile).value
)

addCommandAlias("testJS",  allSubprojectsJS  map (_ + "/test") mkString (";", ";", ""))
addCommandAlias("testJVM", allSubprojectsJVM map (_ + "/test") mkString (";", ";", ""))

val validateCommands = List(
  "clean",
  //"mimaReportBinaryIssues",
  "coverageOff",
  "testJS",
  "coverage",
  "testJVM",
  "scalastyle",
  "test:scalastyle",
  "doc",
  "docs/tut"
)

addCommandAlias("validate", validateCommands.mkString(";", ";", ""))
