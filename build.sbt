import sbtcrossproject.CrossProject
import sbtcrossproject.Platform

/// variables

val groupId = "eu.timepit"
val projectName = "refined"
val rootPkg = s"$groupId.$projectName"
val gitHubOwner = "fthomas"
val gitPubUrl = s"https://github.com/$gitHubOwner/$projectName.git"
val gitDevUrl = s"git@github.com:$gitHubOwner/$projectName.git"

// Remember to update these in .travis.yml, too.
val Scala211 = "2.11.12"
val Scala212 = "2.12.10"
val Scala213 = "2.13.1"

val catsVersion = "2.0.0"
val jsonpathVersion = "2.4.0"
val macroParadiseVersion = "2.1.1"
val pureconfigVersion = "0.12.2"
val shapelessVersion = "2.3.3"
val scalaCheckVersion = "1.14.3"
val scalaXmlVersion = "1.2.0"
val scalazVersion = "7.2.28"
val scodecVersion = "1.11.4"
val scoptVersion = "3.7.1"

def macroParadise(configuration: Configuration): Def.Initialize[Seq[ModuleID]] = Def.setting {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, v)) if v <= 12 =>
      Seq(
        compilerPlugin(
          ("org.scalamacros" % "paradise" % macroParadiseVersion cross CrossVersion.patch) % configuration
        )
      )
    case _ =>
      Seq.empty // https://github.com/scala/scala/pull/6606
  }
}

val scalaCheckDep =
  Def.setting("org.scalacheck" %%% "scalacheck" % scalaCheckVersion)

val moduleCrossPlatformMatrix: Map[String, List[Platform]] = Map(
  "cats" -> List(JVMPlatform, JSPlatform),
  "core" -> List(JVMPlatform, JSPlatform, NativePlatform),
  "eval" -> List(JVMPlatform),
  "jsonpath" -> List(JVMPlatform),
  "pureconfig" -> List(JVMPlatform),
  "scalacheck" -> List(JVMPlatform, JSPlatform),
  "scalaz" -> List(JVMPlatform, JSPlatform, NativePlatform),
  "scodec" -> List(JVMPlatform, JSPlatform),
  "scopt" -> List(JVMPlatform, JSPlatform),
  "shapeless" -> List(JVMPlatform, JSPlatform, NativePlatform)
)

val moduleCrossScalaVersionsMatrix: (String, Platform) => List[String] = {
  case (_, NativePlatform) =>
    List(Scala211)
  case _ =>
    List(Scala211, Scala212, Scala213)
}

def allSubprojectsOf(
    platform: sbtcrossproject.Platform,
    scalaVersions: Set[String] = Set.empty
): List[String] =
  moduleCrossPlatformMatrix
    .collect { case (prj, platforms) if platforms.contains(platform) => prj }
    .filter(prj => scalaVersions.subsetOf(moduleCrossScalaVersionsMatrix(prj, platform).toSet))
    .map(_ + platform.sbtSuffix)
    .toList
    .sorted

val allSubprojectsJVM = allSubprojectsOf(JVMPlatform)
val allSubprojectsJVM213 = allSubprojectsOf(JVMPlatform, Set(Scala213))
val allSubprojectsJS = allSubprojectsOf(JSPlatform)
val allSubprojectsJS213 = allSubprojectsOf(JSPlatform, Set(Scala213))
val allSubprojectsNative = allSubprojectsOf(NativePlatform)

/// projects

lazy val root = project
  .in(file("."))
  .aggregate(benchmark, docs)
  .aggregate(allSubprojectsJVM.map(LocalProject): _*)
  .aggregate(allSubprojectsJS.map(LocalProject): _*)
  .aggregate(allSubprojectsNative.map(LocalProject): _*)
  .disablePlugins(MimaPlugin)
  .settings(commonSettings)
  .settings(noPublishSettings)
  .settings(
    console := (coreJVM / Compile / console).value,
    Test / console := (coreJVM / Test / console).value,
    ThisBuild / Test / parallelExecution := false
  )

lazy val benchmark = project
  .configure(moduleConfig("benchmark"))
  .dependsOn(coreJVM)
  .enablePlugins(JmhPlugin)
  .disablePlugins(MimaPlugin)
  .settings(noPublishSettings)

lazy val cats = myCrossProject("cats")
  .dependsOn(core % "compile->compile;test->test", scalacheck % Test)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core" % catsVersion,
      "org.typelevel" %%% "cats-laws" % catsVersion % Test,
      "org.typelevel" %%% "discipline-scalatest" % "1.0.0-RC4" % Test
    ),
    initialCommands += s"""
      import $rootPkg.cats._
    """
  )

lazy val catsJVM = cats.jvm
lazy val catsJS = cats.js

lazy val core = myCrossProject("core")
  .enablePlugins(BuildInfoPlugin)
  .settings(moduleName := projectName)
  .settings(
    libraryDependencies ++= macroParadise(Compile).value ++ Seq(
      scalaOrganization.value % "scala-reflect" % scalaVersion.value,
      scalaOrganization.value % "scala-compiler" % scalaVersion.value,
      "com.chuusai" %%% "shapeless" % shapelessVersion,
      "org.scala-lang.modules" %% "scala-xml" % scalaXmlVersion,
      scalaCheckDep.value % Test
    ),
    initialCommands += s"""
      import shapeless.tag.@@
    """,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := s"$rootPkg.internal"
  )

lazy val coreJVM = core.jvm
lazy val coreJS = core.js
lazy val coreNative = core.native

lazy val docs = project
  .configure(moduleConfig("docs"))
  .dependsOn(coreJVM)
  .enablePlugins(TutPlugin)
  .disablePlugins(MimaPlugin)
  .settings(noPublishSettings)
  .settings(
    Tut / scalacOptions := scalacOptions.value.diff(Seq("-Ywarn-unused:imports")),
    tutSourceDirectory := baseDirectory.value / "src",
    tutTargetDirectory := baseDirectory.value
  )

lazy val eval = myCrossProject("eval")
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies += scalaOrganization.value % "scala-compiler" % scalaVersion.value,
    initialCommands += s"""
      import $rootPkg.eval._
    """
  )

lazy val evalJVM = eval.jvm

lazy val jsonpath = myCrossProject("jsonpath")
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= macroParadise(Test).value ++ Seq(
      "com.jayway.jsonpath" % "json-path" % jsonpathVersion
    )
  )

lazy val jsonpathJVM = jsonpath.jvm

lazy val pureconfig = myCrossProject("pureconfig")
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= macroParadise(Test).value ++ Seq(
      "com.github.pureconfig" %% "pureconfig-core" % pureconfigVersion,
      "com.github.pureconfig" %% "pureconfig-generic" % pureconfigVersion % Test
    )
  )

lazy val pureconfigJVM = pureconfig.jvm

lazy val scalacheck = myCrossProject("scalacheck")
  .dependsOn(core)
  .settings(
    libraryDependencies += scalaCheckDep.value,
    initialCommands += s"""
      import org.scalacheck.Arbitrary
    """
  )

lazy val scalacheckJVM = scalacheck.jvm
lazy val scalacheckJS = scalacheck.js

lazy val scalaz = myCrossProject("scalaz")
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
lazy val scalazNative = scalaz.native

lazy val scodec = myCrossProject("scodec")
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

lazy val scopt = myCrossProject("scopt")
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

lazy val shapeless = myCrossProject("shapeless")
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    initialCommands += s"""
      import $rootPkg.shapeless._
    """
  )

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

def myCrossProject(name: String): CrossProject =
  CrossProject(name, file(name))(moduleCrossPlatformMatrix(name): _*)
    .configureCross(moduleCrossConfig(name))

def moduleConfig(name: String): Project => Project =
  _.in(file(s"modules/$name"))
    .settings(moduleName := s"$projectName-$name")
    .settings(commonSettings)
    .settings(
      scalaVersion := Scala212,
      crossScalaVersions := moduleCrossScalaVersionsMatrix(name, JVMPlatform)
    )

def moduleCrossConfig(name: String, module: String): CrossProject => CrossProject = {
  val transform = (_: CrossProject)
    .in(file(s"modules/$name"))
    .settings(moduleName := s"$projectName-$module")
    .settings(moduleCrossSettings)

  moduleCrossPlatformMatrix(name).foldRight(transform) {
    case (platform, t) =>
      platform match {
        case JVMPlatform    => t.andThen(_.jvmSettings(moduleJvmSettings(module)))
        case JSPlatform     => t.andThen(_.jsSettings(moduleJsSettings(module)))
        case NativePlatform => t.andThen(_.nativeSettings(moduleNativeSettings(module)))
      }
  }
}

def moduleCrossConfig(name: String): CrossProject => CrossProject =
  moduleCrossConfig(name, name)

lazy val moduleCrossSettings = Def.settings(
  commonSettings
)

def moduleJvmSettings(name: String): Seq[Def.Setting[_]] = Def.settings(
  scalaVersion := Scala212,
  crossScalaVersions := moduleCrossScalaVersionsMatrix(name, JVMPlatform),
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
      ProblemFilters.exclude[IncompatibleSignatureProblem]("*"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("eu.timepit.refined.api.Max.findValid"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("eu.timepit.refined.api.Min.findValid")
    )
  }
)

def moduleJsSettings(name: String): Seq[Def.Setting[_]] = Def.settings(
  scalaVersion := Scala212,
  crossScalaVersions := moduleCrossScalaVersionsMatrix(name, JSPlatform),
  doctestGenTests := Seq.empty,
  mimaFailOnNoPrevious := false
)

def moduleNativeSettings(name: String): Seq[Def.Setting[_]] = Def.settings(
  scalaVersion := Scala211,
  crossScalaVersions := moduleCrossScalaVersionsMatrix(name, NativePlatform),
  // Disable Scaladoc generation because of:
  // [error] dropping dependency on node with no phase object: mixin
  Compile / doc / sources := Seq.empty,
  doctestGenTests := Seq.empty,
  mimaFailOnNoPrevious := false
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
    Developer(
      id = "fthomas",
      name = "Frank S. Thomas",
      email = "",
      url("https://github.com/fthomas")
    )
  )
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
          "-Ywarn-unused:imports"
          //"-Ywarn-unused:locals",
          //"-Ywarn-unused:params",
          //"-Ywarn-unused:patvars"
          //"-Ywarn-unused:privates"
        )
      case _ => Seq("-Xlint")
    }
  },
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 13)) => Seq.empty
      case _             => Seq("-Xfatal-warnings")
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
    Some(
      url(
        s"https://static.javadoc.io/$groupId/${moduleName.value}_$binaryScalaVersion/$refinedVersion/$indexHtml"
      )
    )
  }
)

lazy val noPublishSettings = Def.settings(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

/// commands

def addCommandsAlias(name: String, cmds: Seq[String]): Seq[Def.Setting[State => State]] =
  addCommandAlias(name, cmds.mkString(";", ";", ""))

addCommandsAlias(
  "fmt",
  Seq(
    "scalafmt",
    "test:scalafmt",
    "scalafmtSbt"
  )
)

addCommandsAlias(
  "fmtCheck",
  Seq(
    "scalafmtCheck",
    "test:scalafmtCheck",
    "scalafmtSbtCheck",
    "scalastyle",
    "test:scalastyle"
  )
)

addCommandsAlias("compileNative", allSubprojectsNative.map(_ + "/compile"))
addCommandsAlias("testJS", allSubprojectsJS.map(_ + "/test"))
addCommandsAlias("testJVM", allSubprojectsJVM.map(_ + "/test"))
addCommandsAlias("testJS213", allSubprojectsJS213.map(_ + "/test"))
addCommandsAlias("testJVM213", allSubprojectsJVM213.map(_ + "/test"))

addCommandsAlias(
  "validateJVM",
  Seq(
    "clean",
    "fmtCheck",
    "coverage",
    "mimaReportBinaryIssues",
    "testJVM",
    "coverageReport",
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

addCommandsAlias(
  "validateNative",
  Seq(
    "compileNative"
  )
)
