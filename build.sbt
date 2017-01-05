import org.scalajs.sbtplugin.cross.CrossProject

/// variables

val groupId = "eu.timepit"
val projectName = "refined"
val rootPkg = s"$groupId.$projectName"
val gitHubOwner = "fthomas"
val gitPubUrl = s"https://github.com/$gitHubOwner/$projectName.git"
val gitDevUrl = s"git@github.com:$gitHubOwner/$projectName.git"

val macroCompatVersion = "1.1.1"
val macroParadiseVersion = "2.1.0"
val shapelessVersion = "2.3.2"
val scalaCheckVersion = "1.13.4"
val scalaXmlVersion = "1.0.6"
val scalazVersion = "7.2.8"
val scodecVersion = "1.10.3"
val pureconfigVersion = "0.5.0"

// needed for tests with Scala 2.10
val macroParadise = compilerPlugin(
  "org.scalamacros" % "paradise" % macroParadiseVersion % Test cross CrossVersion.full)

val allSubprojects = Seq("core", "scalacheck", "scalaz", "scodec", "pureconfig")
val allSubprojectsJVM = allSubprojects.map(_ + "JVM")
val allSubprojectsJS = {
  val jvmOnlySubprojects = Seq("pureconfig")
  (allSubprojects diff jvmOnlySubprojects).map(_ + "JS")
}

/// projects

lazy val root = project
  .in(file("."))
  .aggregate(coreJVM,
             coreJS,
             docs,
             scalacheckJVM,
             scalacheckJS,
             scalazJVM,
             scalazJS,
             scodecJVM,
             scodecJS,
             pureconfigJVM)
  .settings(commonSettings)
  .settings(noPublishSettings)
  .settings(releaseSettings)
  .settings(
    console := console.in(coreJVM, Compile).value,
    console.in(Test) := console.in(coreJVM, Test).value,
    parallelExecution in Test in ThisBuild := false
  )

lazy val core = crossProject
  .configureCross(moduleConfig("core"))
  .enablePlugins(BuildInfoPlugin)
  .settings(moduleName := projectName)
  .settings(siteSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-compiler" % scalaVersion.value,
      "org.typelevel" %%% "macro-compat" % macroCompatVersion,
      "com.chuusai" %%% "shapeless" % shapelessVersion,
      "org.scalacheck" %%% "scalacheck" % scalaCheckVersion % Test,
      macroParadise
    ),
    libraryDependencies ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 10)) => Seq.empty
        case _ => Seq("org.scala-lang.modules" %% "scala-xml" % scalaXmlVersion)
      }
    },
    initialCommands += s"""
      import shapeless.tag.@@
    """,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := s"$rootPkg.internal"
  )

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

lazy val docs = project
  .in(file("modules/docs"))
  .dependsOn(coreJVM)
  .settings(moduleName := s"$projectName-docs")
  .settings(commonSettings)
  .settings(noPublishSettings)
  .settings(tutSettings)
  .settings(
    tutScalacOptions := scalacOptions.value,
    tutSourceDirectory := baseDirectory.value / "src",
    tutTargetDirectory := baseDirectory.value
  )

lazy val scalacheck = crossProject
  .configureCross(moduleConfig("scalacheck"))
  .dependsOn(core)
  .settings(
    libraryDependencies += "org.scalacheck" %%% "scalacheck" % scalaCheckVersion,
    initialCommands += s"""
      import org.scalacheck.Arbitrary
    """
  )

lazy val scalacheckJVM = scalacheck.jvm
lazy val scalacheckJS = scalacheck.js

lazy val scalaz = crossProject
  .configureCross(moduleConfig("scalaz"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies += "org.scalaz" %%% "scalaz-core" % scalazVersion,
    initialCommands += s"""
      import $rootPkg.scalaz._
      import $rootPkg.scalaz.auto._
      import _root_.scalaz.@@
    """
  )

lazy val scalazJVM = scalaz.jvm
lazy val scalazJS = scalaz.js

lazy val scodec = crossProject
  .configureCross(moduleConfig("scodec"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= Seq(
      "org.scodec" %%% "scodec-core" % scodecVersion,
      macroParadise
    )
  )

lazy val scodecJVM = scodec.jvm
lazy val scodecJS = scodec.js

lazy val pureconfig = crossProject
  .configureCross(moduleConfig("pureconfig"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= Seq(
      "com.github.melrief" %% "pureconfig" % pureconfigVersion,
      macroParadise
    )
  )

lazy val pureconfigJVM = pureconfig.jvm

/// settings

lazy val commonSettings = Def.settings(
  compileSettings,
  metadataSettings,
  myDoctestSettings,
  scaladocSettings,
  styleSettings,
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
    import $rootPkg.types.all._
    import shapeless.{ ::, HList, HNil }
    import shapeless.nat._
  """
)

def moduleConfig(name: String): CrossProject => CrossProject =
  _.in(file(s"modules/$name"))
    .settings(moduleName := s"$projectName-$name")
    .settings(moduleSettings)
    .jvmSettings(moduleJvmSettings)
    .jsSettings(moduleJsSettings)

lazy val moduleSettings = Def.settings(
  commonSettings,
  publishSettings,
  releaseSettings
)

lazy val moduleJvmSettings = Def.settings(
  mimaPreviousArtifacts := {
    val latestVersionWithoutModules = Set(
      s"$projectName-pureconfig" -> "0.6.0",
      s"$projectName-scalacheck" -> "0.3.0",
      s"$projectName-scalaz" -> "0.3.1",
      s"$projectName-scodec" -> "0.3.1"
    )

    val latestVersionExists =
      !latestVersionWithoutModules.contains {
        moduleName.value -> latestVersion.value
      }

    if (publishArtifact.value && latestVersionExists)
      Set(groupId %% moduleName.value % latestVersion.value)
    else
      Set.empty
  },
  mimaBinaryIssueFilters ++= {
    import com.typesafe.tools.mima.core._
    import com.typesafe.tools.mima.core.ProblemFilters._
    Seq(
      exclude[DirectMissingMethodProblem](
        "eu.timepit.refined.internal.RefinePartiallyApplied.force"))
  }
)

lazy val moduleJsSettings = Def.settings(
  doctestGenTests := Seq.empty
)

lazy val metadataSettings = Def.settings(
  name := projectName,
  description := "Simple refinement types for Scala",
  organization := groupId,
  homepage := Some(url(s"https://github.com/$gitHubOwner/$projectName")),
  startYear := Some(2015),
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  scmInfo := Some(ScmInfo(homepage.value.get, s"scm:git:$gitPubUrl", Some(s"scm:git:$gitDevUrl"))),
  developers := List(
    Developer(id = "fthomas",
              name = "Frank S. Thomas",
              email = "",
              url("https://github.com/fthomas")))
)

lazy val compileSettings = Def.settings(
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
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
  wartremoverErrors in (Compile, compile) ++= Warts.unsafe diff Seq(
    Wart.Any,
    Wart.AsInstanceOf,
    Wart.NonUnitStatements,
    Wart.Null,
    Wart.Throw
  )
)

lazy val scaladocSettings = Def.settings(
  scalacOptions in (Compile, doc) ++= Seq(
    //"-diagrams",
    "-diagrams-debug",
    "-doc-source-url",
    scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
    "-sourcepath",
    baseDirectory.in(LocalRootProject).value.getAbsolutePath
  ),
  autoAPIMappings := true,
  apiURL := Some(url(s"http://$gitHubOwner.github.io/$projectName/latest/api/"))
)

lazy val publishSettings = Def.settings(
  publishMavenStyle := true,
  pomIncludeRepository := { _ =>
    false
  }
)

lazy val noPublishSettings = Def.settings(
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
    val oldVersion = extracted.get(latestVersion)

    val readme = "README.md"
    val oldContent = IO.read(file(readme))
    val newContent = oldContent.replaceAll(oldVersion, newVersion)
    IO.write(file(readme), newContent)
    s"git add $readme" !! st.log

    st
  }

  Def.settings(
    releaseCrossBuild := true,
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    releaseVcsSign := true,
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
      setLatestVersion,
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )
}

lazy val siteSettings = Def.settings(
  site.settings,
  site.includeScaladoc(),
  ghpages.settings,
  git.remoteRepo := gitDevUrl
)

lazy val myDoctestSettings = Def.settings(
  doctestWithDependencies := false
)

lazy val styleSettings = Def.settings(
  scalafmtConfig := Some(file(".scalafmt.conf")),
  reformatOnCompileSettings,
  // workaround for https://github.com/scalastyle/scalastyle-sbt-plugin/issues/47
  scalastyleSources in Compile :=
    (unmanagedSourceDirectories in Compile).value ++
      (unmanagedSourceDirectories in Test).value
)

/// commands

def addCommandsAlias(name: String, cmds: Seq[String]) =
  addCommandAlias(name, cmds.mkString(";", ";", ""))

addCommandsAlias("syncMavenCentral", allSubprojectsJVM.map(_ + "/bintraySyncMavenCentral"))

addCommandsAlias("testJS", allSubprojectsJS.map(_ + "/test"))
addCommandsAlias("testJVM", allSubprojectsJVM.map(_ + "/test"))

addCommandsAlias("validate",
                 Seq(
                   "clean",
                   "scalafmtTest",
                   "scalastyle",
                   "test:scalastyle",
                   "mimaReportBinaryIssues",
                   "testJS",
                   "coverage",
                   "testJVM",
                   "coverageReport",
                   "coverageOff",
                   "doc",
                   "docs/tut"
                 ))
