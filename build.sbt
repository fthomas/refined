import sbtcrossproject.{crossProject, CrossProject, CrossType}
import scala.sys.process._

/// variables

val groupId = "eu.timepit"
val projectName = "refined"
val rootPkg = s"$groupId.$projectName"
val gitHubOwner = "fthomas"
val gitPubUrl = s"https://github.com/$gitHubOwner/$projectName.git"
val gitDevUrl = s"git@github.com:$gitHubOwner/$projectName.git"

val catsVersion = "1.0.1"
val jsonpathVersion = "2.4.0"
val macroCompatVersion = "1.1.1"
val macroParadiseVersion = "2.1.1"
val pureconfigVersion = "0.9.0"
val shapelessVersion = "2.3.3"
val scalaCheckVersion = "1.13.5"
val scalaXmlVersion = "1.0.6"
val scalazVersion = "7.2.19"
val scodecVersion = "1.10.3"

val macroParadise =
  "org.scalamacros" % "paradise" % macroParadiseVersion cross CrossVersion.patch
val scalaCheckDep =
  Def.setting("org.scalacheck" %%% "scalacheck" % scalaCheckVersion)

val allSubprojects =
  Seq("cats", "core", "eval", "jsonpath", "pureconfig", "scalacheck", "scalaz", "scodec")
val allSubprojectsJVM = allSubprojects.map(_ + "JVM")
val allSubprojectsJS = {
  val jvmOnlySubprojects = Seq("jsonpath", "pureconfig")
  (allSubprojects diff jvmOnlySubprojects).map(_ + "JS")
}

val Scala211 = Def.setting(crossScalaVersions.value.find(_.startsWith("2.11")).get)

/// projects

lazy val root = project
  .in(file("."))
  .aggregate(
    benchmark,
    catsJVM,
    catsJS,
    coreJVM,
    coreJS,
    docs,
    evalJVM,
    evalJS,
    jsonpathJVM,
    pureconfigJVM,
    scalacheckJVM,
    scalacheckJS,
    scalazJVM,
    scalazJS,
    scodecJVM,
    scodecJS
  )
  .settings(commonSettings)
  .settings(noPublishSettings)
  .settings(releaseSettings)
  .settings(
    console := console.in(coreJVM, Compile).value,
    console.in(Test) := console.in(coreJVM, Test).value,
    parallelExecution in Test in ThisBuild := false
  )

lazy val benchmark = project
  .configure(moduleConfig("benchmark"))
  .dependsOn(coreJVM)
  .enablePlugins(JmhPlugin)
  .settings(noPublishSettings)

lazy val cats = crossProject(JSPlatform, JVMPlatform)
  .configureCross(moduleCrossConfig("cats"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies += "org.typelevel" %%% "cats-core" % catsVersion,
    initialCommands += s"""
      import $rootPkg.cats._
    """
  )

lazy val catsJVM = cats.jvm
lazy val catsJS = cats.js

lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .configureCross(moduleCrossConfig("core"))
  .enablePlugins(BuildInfoPlugin)
  .settings(moduleName := projectName)
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin(macroParadise),
      scalaOrganization.value % "scala-reflect" % scalaVersion.value,
      scalaOrganization.value % "scala-compiler" % scalaVersion.value,
      "org.typelevel" %% "macro-compat" % macroCompatVersion,
      "com.chuusai" %%% "shapeless" % shapelessVersion,
      scalaCheckDep.value % Test
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
  .nativeSettings(
    libraryDependencies -= scalaCheckDep.value % Test,
    // Disable Scaladoc generation because of:
    // [error] dropping dependency on node with no phase object: mixin
    publishArtifact in packageDoc := false,
    sources in (Compile, doc) := Seq.empty
  )

lazy val coreJVM = core.jvm
lazy val coreJS = core.js
lazy val coreNative = core.native

lazy val docs = project
  .configure(moduleConfig("docs"))
  .dependsOn(coreJVM)
  .enablePlugins(TutPlugin)
  .settings(noPublishSettings)
  .settings(
    scalacOptions in Tut := scalacOptions.value.diff(Seq("-Ywarn-unused:imports")),
    tutSourceDirectory := baseDirectory.value / "src",
    tutTargetDirectory := baseDirectory.value
  )

lazy val eval = crossProject(JSPlatform, JVMPlatform)
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

lazy val jsonpath = crossProject(JSPlatform, JVMPlatform)
  .configureCross(moduleCrossConfig("jsonpath"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= Seq(
      "com.jayway.jsonpath" % "json-path" % jsonpathVersion,
      compilerPlugin(macroParadise % Test)
    )
  )

lazy val jsonpathJVM = jsonpath.jvm

lazy val pureconfig = crossProject(JSPlatform, JVMPlatform)
  .configureCross(moduleCrossConfig("pureconfig"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= Seq(
      "com.github.pureconfig" %% "pureconfig" % pureconfigVersion,
      compilerPlugin(macroParadise % Test)
    )
  )

lazy val pureconfigJVM = pureconfig.jvm

lazy val scalacheck = crossProject(JSPlatform, JVMPlatform)
  .configureCross(moduleCrossConfig("scalacheck"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies += scalaCheckDep.value,
    initialCommands += s"""
      import org.scalacheck.Arbitrary
    """
  )

lazy val scalacheckJVM = scalacheck.jvm
lazy val scalacheckJS = scalacheck.js

lazy val scalaz = crossProject(JSPlatform, JVMPlatform)
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

lazy val scodec = crossProject(JSPlatform, JVMPlatform)
  .configureCross(moduleCrossConfig("scodec"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= Seq(
      "org.scodec" %%% "scodec-core" % scodecVersion,
      compilerPlugin(macroParadise % Test)
    ),
    initialCommands += s"""
      import $rootPkg.scodec.predicates.byteVector._
      import _root_.scodec.bits.ByteVector
    """
  )

lazy val scodecJVM = scodec.jvm
lazy val scodecJS = scodec.js

/// settings

lazy val commonSettings = Def.settings(
  compileSettings,
  metadataSettings,
  scaladocSettings,
  initialCommands := s"""
    import $rootPkg._
    import $rootPkg.api._
    import $rootPkg.api.Inference.==>
    import $rootPkg.api.RefType.ops._
    import $rootPkg.auto._
    import $rootPkg.predicates.all._
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
    val hasPredecessor = !unreleasedModules.value.contains(moduleName.value)
    latestVersionInSeries.value match {
      case Some(latest) if publishArtifact.value && hasPredecessor =>
        Set(groupId %% moduleName.value % latest)
      case _ =>
        Set.empty
    }
  },
  mimaBinaryIssueFilters ++= {
    import com.typesafe.tools.mima.core._
    Seq(
      ProblemFilters.exclude[ReversedMissingMethodProblem](
        "eu.timepit.refined.scalacheck.StringInstances.nonEmptyStringArbitrary"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("eu.timepit.refined.api.Refined.get"),
      ProblemFilters.exclude[DirectMissingMethodProblem](
        "eu.timepit.refined.api.Refined.get$extension"),
      ProblemFilters.exclude[MissingClassProblem]("eu.timepit.refined.util.time$"),
      ProblemFilters.exclude[MissingClassProblem]("eu.timepit.refined.util.time"),
      ProblemFilters.exclude[DirectMissingMethodProblem](
        "eu.timepit.refined.numeric.moduloValidateNat"),
      ProblemFilters.exclude[DirectMissingMethodProblem](
        "eu.timepit.refined.numeric.moduloValidateWit"),
      ProblemFilters.exclude[DirectMissingMethodProblem](
        "eu.timepit.refined.NumericValidate.moduloValidateNat"),
      ProblemFilters.exclude[DirectMissingMethodProblem](
        "eu.timepit.refined.NumericValidate.moduloValidateWit"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("eu.timepit.refined.types.*"),
      ProblemFilters.exclude[IncompatibleResultTypeProblem]("eu.timepit.refined.types.*"),
      ProblemFilters.exclude[MissingClassProblem]("eu.timepit.refined.types.*"),
      ProblemFilters.exclude[MissingTypesProblem]("eu.timepit.refined.types.*"),
      ProblemFilters.exclude[ReversedMissingMethodProblem]("eu.timepit.refined.types.*")
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
  crossScalaVersions += "2.13.0-M3",
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
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, minor)) if minor >= 12 =>
        Seq(
          "-Xlint:-unused,_",
          //"-Ywarn-unused:implicits",
          "-Ywarn-unused:imports",
          //"-Ywarn-unused:locals",
          //"-Ywarn-unused:params",
          //"-Ywarn-unused:patvars"
          //"-Ywarn-unused:privates"
        )
      case _ => Seq("-Xlint")
    }
  },
  scalacOptions in (Compile, console) -= "-Ywarn-unused:imports",
  scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value
)

lazy val scaladocSettings = Def.settings(
  scalacOptions in (Compile, doc) ++= {
    val tag = s"v${version.value}"
    val tree = if (isSnapshot.value) "master" else tag
    Seq(
      //"-diagrams",
      "-diagrams-debug",
      "-doc-source-url",
      s"${scmInfo.value.get.browseUrl}/blob/${tree}€{FILE_PATH}.scala",
      "-sourcepath",
      baseDirectory.in(LocalRootProject).value.getAbsolutePath
    )
  },
  autoAPIMappings := true,
  apiURL := {
    val binaryScalaVersion = CrossVersion.binaryScalaVersion(scalaVersion.value)
    val refinedVersion = if (isSnapshot.value) latestVersion.value else version.value
    val indexHtml = rootPkg.replace('.', '/') + "/index.html"
    Some(url(
      s"https://static.javadoc.io/$groupId/${moduleName.value}_$binaryScalaVersion/$refinedVersion/$indexHtml"))
  }
)

lazy val publishSettings = Def.settings(
  publishMavenStyle := true,
  pomIncludeRepository := { _ =>
    false
  }
)

lazy val noPublishSettings = Def.settings(
  publish := {},
  publishLocal := {},
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
      releaseStepCommand(s"++${Scala211.value}"),
      releaseStepCommand("coreNative/publishSigned"),
      setLatestVersion,
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )
}

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
    "scalafmtCheck",
    "scalafmtSbtCheck",
    "test:scalafmtCheck",
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
