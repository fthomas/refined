latestVersion in ThisBuild := "0.8.7"

latestVersionInSeries in ThisBuild := Some("0.8.7")

unreleasedModules in ThisBuild := Set(
  "refined-scopt",
  "refined-shapeless"
  // Example:
  // "refined-eval"
)
