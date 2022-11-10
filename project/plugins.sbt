scalaVersion := "2.12.17" // remove once sbt uses 2.12.17 or newer

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.11.0")

addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.5.11")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")

addSbtPlugin("com.github.tkawachi" % "sbt-doctest" % "0.10.0")

addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "1.1.1")

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.2.0")

addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.2.0")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.11.0")

addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.4.8")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.6")

addSbtPlugin("org.typelevel" % "sbt-typelevel-mergify" % "0.4.16")

addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.4.3")
