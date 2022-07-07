ThisBuild / latestVersion := "0.10.1"

ThisBuild / bincompatVersions := Map(
  "2.12" -> Set(
    "0.10.0",
    "0.10.1"
    // NEXT_VERSION
  ),
  "2.13" -> Set(
    "0.10.0",
    "0.10.1"
    // NEXT_VERSION
  )
)

ThisBuild / unreleasedModules := Set(
  // Example:
  // "refined-eval"
)
