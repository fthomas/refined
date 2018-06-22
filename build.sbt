import sbtcrossproject.CrossPlugin.autoImport.crossProject
import sbtcrossproject.CrossProject
import scala.sys.process._

/// variables

val groupId = "eu.timepit"
val projectName = "refined"
val rootPkg = s"$groupId.$projectName"
val gitHubOwner = "fthomas"
val gitPubUrl = s"https://github.com/$gitHubOwner/$projectName.git"
val gitDevUrl = s"git@github.com:$gitHubOwner/$projectName.git"

val catsVersion = "1.1.0"
val jsonpathVersion = "2.4.0"
val macroCompatVersion = "1.1.1"
val macroParadiseVersion = "2.1.1"
val pureconfigVersion = "0.9.0"
val shapelessVersion = "2.3.3"
val scalaCheckVersion = "1.14.0"
val scalaXmlVersion = "1.1.0"
val scalazVersion = "7.2.22"
val scodecVersion = "1.10.3"
val scoptVersion = "3.7.0"

def macroParadise(configuration: Configuration) = Def.setting {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, v)) if v <= 12 =>
      Seq(compilerPlugin(
        ("org.scalamacros" % "paradise" % macroParadiseVersion cross CrossVersion.patch) % configuration))
    case _ =>
      Seq.empty // https://github.com/scala/scala/pull/6606
  }
}

val scalaCheckDep =
  Def.setting("org.scalacheck" %%% "scalacheck" % scalaCheckVersion)

val allSubprojects =
  Seq("cats",
      "core",
      "eval",
      "jsonpath",
      "pureconfig",
      "scalacheck",
      "scalaz",
      "scodec",
      "scopt",
      "shapeless")

val allSubprojectsJVM = allSubprojects.map(_ + "JVM")
val allSubprojectsJS = {
  val jvmOnlySubprojects = Seq("jsonpath", "pureconfig")
  (allSubprojects diff jvmOnlySubprojects).map(_ + "JS")
}
val allSubprojectsNative = Seq("core", "scalaz", "shapeless").map(_ + "Native")

// Remember to update these in .travis.yml, too.
val Scala211 = "2.11.12"
val Scala213 = "2.13.0-M4"

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
    scodecJS,
    scoptJVM,
    scoptJS,
    shapelessJVM,
    shapelessJS
  )
  .settings(commonSettings)
  .settings(noPublishSettings)
  .settings(releaseSettings)
  .settings(
    console := (coreJVM / Compile / console).value,
    Test / console := (coreJVM / Test / console).value,
    ThisBuild / Test / parallelExecution := false
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
    crossScalaVersions += Scala213,
    libraryDependencies ++= macroParadise(Compile).value ++ Seq(
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
    moduleNativeSettings
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
    Tut / scalacOptions := scalacOptions.value.diff(Seq("-Ywarn-unused:imports")),
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
    libraryDependencies ++= macroParadise(Test).value ++ Seq(
      "com.jayway.jsonpath" % "json-path" % jsonpathVersion
    )
  )

lazy val jsonpathJVM = jsonpath.jvm

lazy val pureconfig = crossProject(JSPlatform, JVMPlatform)
  .configureCross(moduleCrossConfig("pureconfig"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= macroParadise(Test).value ++ Seq(
      "com.github.pureconfig" %% "pureconfig" % pureconfigVersion
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

lazy val scalaz = crossProject(JSPlatform, JVMPlatform, NativePlatform)
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
  .nativeSettings(moduleNativeSettings)

lazy val scalazJVM = scalaz.jvm
lazy val scalazJS = scalaz.js
lazy val scalazNative = scalaz.native

lazy val scodec = crossProject(JSPlatform, JVMPlatform)
  .configureCross(moduleCrossConfig("scodec"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= macroParadise(Test).value ++ Seq(
      "org.scodec" %%% "scodec-core" % scodecVersion
    ),
    initialCommands += s"""
      import $rootPkg.scodec.predicates.byteVector._
      import _root_.scodec.bits.ByteVector
    """
  )

lazy val scodecJVM = scodec.jvm
lazy val scodecJS = scodec.js

lazy val scopt = crossProject(JSPlatform, JVMPlatform)
  .configureCross(moduleCrossConfig("scopt"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= macroParadise(Test).value ++ Seq(
      "com.github.scopt" %%% "scopt" % scoptVersion
    )
  )
  // This is required by scopt (which uses the 'os' modue), although I'm not 100% sure what other potential side effects
  // this might have.
  .jsSettings(scalaJSModuleKind := ModuleKind.CommonJSModule)

lazy val scoptJVM = scopt.jvm
lazy val scoptJS = scopt.js

lazy val shapeless = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .configureCross(moduleCrossConfig("shapeless"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    initialCommands += s"""
      import $rootPkg.shapeless._
    """
  )
  .nativeSettings(moduleNativeSettings)

lazy val shapelessJVM = shapeless.jvm
lazy val shapelessJS = shapeless.js
lazy val shapelessNative = shapeless.native

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
    if (hasPredecessor && publishArtifact.value)
      bincompatVersions.value.map(v => groupId %% moduleName.value % v)
    else
      Set.empty
  },
  mimaBinaryIssueFilters ++= {
    import com.typesafe.tools.mima.core._
    Seq(
      )
  }
)

lazy val moduleJsSettings = Def.settings(
  doctestGenTests := Seq.empty
)

lazy val moduleNativeSettings = Def.settings(
  scalaVersion := Scala211,
  crossScalaVersions := Seq(Scala211),
  // Disable Scaladoc generation because of:
  // [error] dropping dependency on node with no phase object: mixin
  Compile / doc / sources := Seq.empty,
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
  crossScalaVersions -= Scala213,
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
    //"-Xfatal-warnings",
    "-Xfuture",
    //"-Xlog-implicits",
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
  Compile / console / scalacOptions -= "-Ywarn-unused:imports",
  Test / console / scalacOptions := (Compile / console / scalacOptions).value
)

lazy val scaladocSettings = Def.settings(
  Compile / doc / scalacOptions ++= {
    val tag = s"v${version.value}"
    val tree = if (isSnapshot.value) "master" else tag
    Seq(
      //"-diagrams",
      "-diagrams-debug",
      "-doc-source-url",
      s"${scmInfo.value.get.browseUrl}/blob/${tree}€{FILE_PATH}.scala",
      "-sourcepath",
      (LocalRootProject / baseDirectory).value.getAbsolutePath
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
      releaseStepCommand(s"++$Scala211"),
      releaseStepCommand("coreNative/publishSigned"),
      releaseStepCommand("scalazNative/publishSigned"),
      releaseStepCommand("shapelessNative/publishSigned"),
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

addCommandsAlias("compileNative", allSubprojectsNative.map(_ + "/compile"))
addCommandsAlias("testJS", allSubprojectsJS.map(_ + "/test"))
addCommandsAlias("testJVM", allSubprojectsJVM.map(_ + "/test"))

addCommandsAlias(
  "validateJVM",
  Seq(
    "clean",
    "scalafmtCheck",
    "scalafmtSbtCheck",
    "test:scalafmtCheck",
    "scalastyle",
    "test:scalastyle",
    "mimaReportBinaryIssues",
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

addCommandsAlias(
  "validateJS",
  Seq(
    "testJS"
  )
)
