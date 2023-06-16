ThisBuild / latestVersion := "0.11.0"

ThisBuild / bincompatVersions := Map(
  "2.12" -> Set(
    "0.11.0"
    // NEXT_VERSION
  ),
  "2.13" -> Set(
    "0.11.0"
    // NEXT_VERSION
  ),
  "3" -> Set(
    "0.11.0"
    // NEXT_VERSION
  )
)

ThisBuild / unreleasedModules := Set(
  // Example:
  // "refined-eval"
)
