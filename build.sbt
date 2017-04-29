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
val scalazVersion = "7.2.10"
val scodecVersion = "1.10.3"
val pureconfigVersion = "0.7.0"

// needed for tests with Scala 2.10
val macroParadise = compilerPlugin(
  "org.scalamacros" % "paradise" % macroParadiseVersion % Test cross CrossVersion.patch)

val allSubprojects =
  Seq("core", "eval", "scalacheck", "scalaz", "scodec", "pureconfig")
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
             evalJVM,
             evalJS,
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
  .configureCross(moduleCrossConfig("core"))
  .enablePlugins(BuildInfoPlugin)
  .settings(moduleName := projectName)
  .settings(siteSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      scalaOrganization.value % "scala-reflect" % scalaVersion.value,
      scalaOrganization.value % "scala-compiler" % scalaVersion.value,
      "org.typelevel" %%% "macro-compat" % macroCompatVersion,
      "com.chuusai" %%% "shapeless" % shapelessVersion,
      "org.scalacheck" %%% "scalacheck" % scalaCheckVersion % Test,
      macroParadise
    ),
    libraryDependencies ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 10)) => Seq.empty
        case _ =>
          Seq("org.scala-lang.modules" %% "scala-xml" % scalaXmlVersion)
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
  .configure(moduleConfig("docs"))
  .dependsOn(coreJVM)
  .settings(noPublishSettings)
  .settings(tutSettings)
  .settings(
    tutScalacOptions := scalacOptions.value,
    tutSourceDirectory := baseDirectory.value / "src",
    tutTargetDirectory := baseDirectory.value
  )

lazy val eval = crossProject
  .configureCross(moduleCrossConfig("eval"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies += scalaOrganization.value % "scala-compiler" % scalaVersion.value,
    initialCommands += s"""
      import $rootPkg.eval._
    """
  )

lazy val evalJVM = eval.jvm
lazy val evalJS = eval.js

lazy val scalacheck = crossProject
  .configureCross(moduleCrossConfig("scalacheck"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies += "org.scalacheck" %%% "scalacheck" % scalaCheckVersion,
    initialCommands += s"""
      import org.scalacheck.Arbitrary
    """
  )

lazy val scalacheckJVM = scalacheck.jvm
lazy val scalacheckJS = scalacheck.js

lazy val scalaz = crossProject
  .configureCross(moduleCrossConfig("scalaz"))
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
  .configureCross(moduleCrossConfig("scodec"))
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
  .configureCross(moduleCrossConfig("pureconfig"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= Seq(
      "com.github.pureconfig" %% "pureconfig" % pureconfigVersion,
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

def moduleConfig(name: String): Project => Project =
  _.in(file(s"modules/$name"))
    .settings(moduleName := s"$projectName-$name")
    .settings(commonSettings)

def moduleCrossConfig(name: String): CrossProject => CrossProject =
  _.in(file(s"modules/$name"))
    .settings(moduleName := s"$projectName-$name")
    .settings(moduleCrossSettings)
    .jvmSettings(moduleJvmSettings)
    .jsSettings(moduleJsSettings)

lazy val moduleCrossSettings = Def.settings(
  commonSettings,
  publishSettings,
  releaseSettings
)

lazy val moduleJvmSettings = Def.settings(
  mimaPreviousArtifacts := {

    val hasNoPredecessor = unreleasedModules.value contains moduleName.value

    latestVersionInSeries.value match {
      case Some(latest) if publishArtifact.value && !hasNoPredecessor => {
        Set(groupId %% moduleName.value % latest)
      }
      case other => Set.empty
    }

  },
  mimaBinaryIssueFilters ++= {
    import com.typesafe.tools.mima.core._
    import com.typesafe.tools.mima.core.ProblemFilters._
    Seq(
      ProblemFilters.exclude[ReversedMissingMethodProblem](
        "eu.timepit.refined.NumericValidate.moduloValidateWit"),
      ProblemFilters.exclude[ReversedMissingMethodProblem](
        "eu.timepit.refined.NumericValidate.moduloValidateNat"),
      ProblemFilters.exclude[MissingClassProblem]("eu.timepit.refined.scalacheck.util.OurMath"),
      ProblemFilters.exclude[MissingClassProblem]("eu.timepit.refined.scalacheck.util.OurMath$")
    )
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
    //"-Xlog-implicits",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard"
  ),
  scalacOptions += {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 12)) => "-Xlint:-unused,_"
      case _ => "-Xlint"
    }
  },
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
      releaseStepTask(GhPagesKeys.pushSite in LocalProject("coreJVM")),
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

addCommandsAlias(
  "validate",
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
    "docs/tut",
    "package",
    "packageSrc"
  )
)
