addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.4.0")

// workaround until sbt-scalariform depends on 0.1.7:
libraryDependencies += "org.scalariform" %% "scalariform" % "0.1.7"
