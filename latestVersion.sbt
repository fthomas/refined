latestVersion in ThisBuild := "0.9.2"

bincompatVersions in ThisBuild := Set(
  "0.9.0",
  "0.9.1",
  "0.9.2"
)

unreleasedModules in ThisBuild := Set(
  // Example:
  // "refined-eval"
  "refined-scalacheck_1.13"
)
