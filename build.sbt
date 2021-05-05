import sbtcrossproject.CrossProject
import sbtcrossproject.Platform

/// variables

val groupId = "eu.timepit"
val projectName = "refined"
val rootPkg = s"$groupId.$projectName"
val gitHubOwner = "fthomas"
val gitPubUrl = s"https://github.com/$gitHubOwner/$projectName.git"
val gitDevUrl = s"git@github.com:$gitHubOwner/$projectName.git"

val Scala_2_12 = "2.12.13"
val Scala_2_13 = "2.13.5"
val Scala_3_0_0_RC2 = "3.0.0-RC2"
val Scala_3_0_0_RC3 = "3.0.0-RC3"

val catsVersion = "2.6.0"
val jsonpathVersion = "2.4.0"
val macroParadiseVersion = "2.1.1"
val pureconfigVersion = "0.15.0"
val shapelessVersion = "2.3.5"
val scalaCheckVersion = "1.15.4"
val scalazVersion = "7.3.3"
val scodecVersion = "1.11.7"
val scoptVersion = "4.0.1"

def macroParadise(configuration: Configuration): Def.Initialize[Seq[ModuleID]] =
  Def.setting {
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

val moduleCrossPlatformMatrix: Map[String, List[Platform]] = Map(
  "cats" -> List(JVMPlatform, JSPlatform),
  "core" -> List(JVMPlatform, JSPlatform),
  "eval" -> List(JVMPlatform),
  "jsonpath" -> List(JVMPlatform),
  "pureconfig" -> List(JVMPlatform),
  "scalacheck" -> List(JVMPlatform, JSPlatform),
  "scalaz" -> List(JVMPlatform),
  "scodec" -> List(JVMPlatform, JSPlatform),
  "scopt" -> List(JVMPlatform),
  "shapeless" -> List(JVMPlatform, JSPlatform)
)

val moduleCrossScalaVersionsMatrix: (String, Platform) => List[String] = {
  case ("core", _)       => List(Scala_2_12, Scala_2_13, Scala_3_0_0_RC2, Scala_3_0_0_RC3)
  case ("scalacheck", _) => List(Scala_2_12, Scala_2_13, Scala_3_0_0_RC2, Scala_3_0_0_RC3)
  case _                 => List(Scala_2_12, Scala_2_13)
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
val allSubprojectsJVM30 = allSubprojectsOf(JVMPlatform, Set(Scala_3_0_0_RC2, Scala_3_0_0_RC3))
val allSubprojectsJS = allSubprojectsOf(JSPlatform)
val allSubprojectsJS30 = allSubprojectsOf(JSPlatform, Set(Scala_3_0_0_RC2, Scala_3_0_0_RC3))
val allSubprojectsNative = allSubprojectsOf(NativePlatform)

/// sbt-github-actions configuration

ThisBuild / crossScalaVersions := Seq(Scala_2_12, Scala_2_13, Scala_3_0_0_RC2, Scala_3_0_0_RC3)
ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowPublishTargetBranches := Seq(
  RefPredicate.Equals(Ref.Branch("master")),
  RefPredicate.StartsWith(Ref.Tag("v"))
)
ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Run(
    List("sbt ci-release"),
    name = Some("Publish JARs"),
    env = Map(
      "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
    )
  )
)
ThisBuild / githubWorkflowJavaVersions := Seq("adopt@1.8")
ThisBuild / githubWorkflowBuild :=
  Seq(
    WorkflowStep.Sbt(
      List("validateJVM", "validateJS"),
      name = Some("Build project (Scala 2)"),
      cond = Some(s"matrix.scala == '$Scala_2_12' || matrix.scala == '$Scala_2_13'")
    ),
    WorkflowStep.Sbt(
      List("validateJVM30", "validateJS30"),
      name = Some("Build project (Scala 3)"),
      cond = Some(s"matrix.scala == '$Scala_3_0_0_RC2' || matrix.scala == '$Scala_3_0_0_RC3'")
    ),
    WorkflowStep.Use(UseRef.Public("codecov", "codecov-action", "v1"), name = Some("Codecov"))
  )

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
    crossScalaVersions := Nil,
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
      "org.typelevel" %%% "discipline-scalatest" % "2.1.4" % Test
    ),
    initialCommands += s"""
      import $rootPkg.cats._
    """
  )

lazy val catsJVM = cats.jvm
lazy val catsJS = cats.js
lazy val isScala3Setting = Def.setting {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _)) => true
    case _            => false
  }
}

lazy val core = myCrossProject("core")
  .enablePlugins(BuildInfoPlugin)
  .settings(moduleName := projectName)
  .settings(
    libraryDependencies ++= {
      macroParadise(Compile).value ++ (
        if (isScala3Setting.value)
          Seq(
            "org.scala-lang.modules" %% "scala-xml" % "2.0.0-RC1"
          )
        else
          Seq(
            scalaOrganization.value % "scala-reflect" % scalaVersion.value,
            scalaOrganization.value % "scala-compiler" % scalaVersion.value,
            "org.scala-lang.modules" %% "scala-xml" % "1.3.0"
          )
      ) ++ Seq(
        ("com.chuusai" %%% "shapeless" % shapelessVersion).cross(CrossVersion.for3Use2_13),
        "org.scalacheck" %%% "scalacheck" % scalaCheckVersion % Test
      )
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
  .disablePlugins(MimaPlugin)
  .settings(noPublishSettings)

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
    libraryDependencies += "org.scalacheck" %%% "scalacheck" % scalaCheckVersion,
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

lazy val scoptJVM = scopt.jvm

lazy val shapeless = myCrossProject("shapeless")
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    initialCommands += s"""
      import $rootPkg.shapeless._
    """
  )

lazy val shapelessJVM = shapeless.jvm
lazy val shapelessJS = shapeless.js

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
      scalaVersion := Scala_2_13,
      crossScalaVersions := moduleCrossScalaVersionsMatrix(name, JVMPlatform)
    )

def moduleCrossConfig(name: String, module: String): CrossProject => CrossProject = {
  val transform = (_: CrossProject)
    .in(file(s"modules/$name"))
    .settings(moduleName := s"$projectName-$module")
    .settings(moduleCrossSettings)

  moduleCrossPlatformMatrix(name).foldRight(transform) { case (platform, t) =>
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

def moduleJvmSettings(name: String): Seq[Def.Setting[_]] =
  Def.settings(
    scalaVersion := Scala_2_13,
    javaOptions ++= Seq("-Duser.language=en"),
    Test / fork := true,
    crossScalaVersions := moduleCrossScalaVersionsMatrix(name, JVMPlatform),
    mimaPreviousArtifacts := {
      val hasPredecessor = !unreleasedModules.value.contains(moduleName.value)
      bincompatVersions.value
        .get(scalaBinaryVersion.value)
        .filter(_ => hasPredecessor && publishArtifact.value)
        .fold(Set.empty[ModuleID])(_.map(v => groupId %% moduleName.value % v))
    },
    mimaBinaryIssueFilters ++= {
      import com.typesafe.tools.mima.core._
      Seq(
        ProblemFilters.exclude[DirectMissingMethodProblem]("eu.timepit.refined.api.Max.findValid"),
        ProblemFilters.exclude[DirectMissingMethodProblem]("eu.timepit.refined.api.Min.findValid")
      )
    }
  )

def moduleJsSettings(name: String): Seq[Def.Setting[_]] =
  Def.settings(
    scalaVersion := Scala_2_13,
    crossScalaVersions := moduleCrossScalaVersionsMatrix(name, JSPlatform),
    doctestGenTests := Seq.empty,
    mimaFailOnNoPrevious := false,
    coverageEnabled := false,
    scalacOptions += {
      val tagOrHash =
        if (!isSnapshot.value) s"v${version.value}"
        else git.gitHeadCommit.value.getOrElse("master")
      val local = (LocalRootProject / baseDirectory).value.toURI.toString
      val remote = s"https://raw.githubusercontent.com/$gitHubOwner/$projectName/$tagOrHash/"
      val opt = if (isScala3Setting.value) "-scalajs-mapSourceURI" else "-P:scalajs:mapSourceURI"
      s"$opt:$local->$remote"
    }
  )

def moduleNativeSettings(name: String): Seq[Def.Setting[_]] =
  Def.settings(
    crossScalaVersions := moduleCrossScalaVersionsMatrix(name, NativePlatform),
    doctestGenTests := Seq.empty,
    mimaFailOnNoPrevious := false,
    coverageEnabled := false
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
    //"-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:existentials,experimental.macros,higherKinds,implicitConversions",
    "-unchecked",
    "-Xfatal-warnings"
  ),
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, minor)) if minor >= 12 =>
        Seq(
          "-Xlint:-unused,_",
          "-Ywarn-numeric-widen",
          "-Ywarn-value-discard",
          "-Ywarn-unused:implicits",
          "-Ywarn-unused:imports"
          //"-Ywarn-unused:locals",
          //"-Ywarn-unused:params",
          //"-Ywarn-unused:patvars"
          //"-Ywarn-unused:privates"
        )
      case _ => Seq.empty
    }
  },
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, minor)) if minor >= 13 =>
        Seq("-Xlint:-byname-implicit")
      case _ => Seq.empty
    }
  },
  Compile / console / scalacOptions -= "-Ywarn-unused:imports",
  Test / console / scalacOptions := (Compile / console / scalacOptions).value,
  Seq(Compile, Test).map { config =>
    (config / unmanagedSourceDirectories) ++= {
      (config / unmanagedSourceDirectories).value.flatMap { dir: File =>
        if (dir.getName != "scala") Seq(dir)
        else
          CrossVersion.partialVersion(scalaVersion.value) match {
            case Some((2, 12)) => Seq(file(dir.getPath + "-3.0-"))
            case Some((2, 13)) => Seq(file(dir.getPath + "-3.0-"))
            case Some((0, _))  => Seq(file(dir.getPath + "-3.0+"))
            case Some((3, 0))  => Seq(file(dir.getPath + "-3.0+"))
            case other         => sys.error(s"unmanagedSourceDirectories for scalaVersion $other not set")
          }
      }
    }
  }
)

lazy val scaladocSettings = Def.settings(
  Compile / doc / sources := {
    val result = (Compile / doc / sources).value
    if (isScala3Setting.value) Seq.empty else result
  },
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
  publish / skip := true
)

/// commands

def addCommandsAlias(name: String, cmds: Seq[String]): Seq[Def.Setting[State => State]] =
  addCommandAlias(name, cmds.mkString(";", ";", ""))

addCommandsAlias(
  "fmt",
  Seq(
    "scalafmtAll",
    "scalafmtSbt"
  )
)

addCommandsAlias(
  "fmtCheck",
  Seq(
    "scalafmtCheckAll",
    "scalafmtSbtCheck"
  )
)

addCommandsAlias("compileNative", allSubprojectsNative.map(_ + "/compile"))
addCommandsAlias("testJS", allSubprojectsJS.map(_ + "/test"))
addCommandsAlias("testJVM", allSubprojectsJVM.map(_ + "/test"))

addCommandsAlias("testJS30", allSubprojectsJS30.map(_ + "/test"))
addCommandsAlias("testJVM30", allSubprojectsJVM30.map(_ + "/test"))

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

addCommandsAlias(
  "validateJVM30",
  Seq(
    "clean",
    "testJVM30"
  )
)

addCommandsAlias(
  "validateJS30",
  Seq(
    "testJS30"
  )
)
