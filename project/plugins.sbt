ThisBuild / evictionErrorLevel := Level.Info

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.12.0")

addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.8.0")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")

addSbtPlugin("com.github.tkawachi" % "sbt-doctest" % "0.10.0")

addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "1.1.4")

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.3.2")

addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.3.2")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.17.0")

addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.5.5")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.2.1")

addSbtPlugin("org.typelevel" % "sbt-typelevel-mergify" % "0.7.4")

addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.4.7")
