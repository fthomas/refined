addSbtPlugin(
  "org.scala-native" % "sbt-scala-native" % "0.3.6"
    exclude ("org.scala-native", "sbt-crossproject") // https://github.com/portable-scala/sbt-crossproject/issues/72
)
