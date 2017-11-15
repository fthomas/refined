latestVersion in ThisBuild := "0.8.4"

latestVersionInSeries in ThisBuild := Some("0.8.4")

unreleasedModules in ThisBuild := Set(
  // Example:
  // "refined-eval"
  "refined-macros"
)
