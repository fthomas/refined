ThisBuild / evictionErrorLevel := Level.Info

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.13.1")

addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.9.2")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")

addSbtPlugin("io.github.sbt-doctest" % "sbt-doctest" % "0.11.1")

addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "1.1.4")

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.3.2")

addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.3.2")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.17.0")

addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.5.6")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.2.2")

addSbtPlugin("org.typelevel" % "sbt-typelevel-mergify" % "0.7.4")

addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.4.7")
