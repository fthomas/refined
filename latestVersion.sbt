ThisBuild / latestVersion := "0.11.1"

ThisBuild / bincompatVersions := Map(
  "2.12" -> Set(
    "0.11.0",
    "0.11.1"
    // NEXT_VERSION
  ),
  "2.13" -> Set(
    "0.11.0",
    "0.11.1"
    // NEXT_VERSION
  ),
  "3" -> Set(
    "0.11.0",
    "0.11.1"
    // NEXT_VERSION
  )
)

ThisBuild / unreleasedModules := Set(
  // Example:
  // "refined-eval"
)
