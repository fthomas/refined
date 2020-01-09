latestVersion in ThisBuild := "0.9.10"

bincompatVersions in ThisBuild := Set(
  "0.9.3",
  "0.9.4",
  "0.9.5",
  "0.9.6",
  "0.9.7",
  "0.9.8",
  "0.9.9",
  "0.9.10"
  // NEXT_VERSION
)

unreleasedModules in ThisBuild := Set(
  // Example:
  // "refined-eval"
  "refined-scalaxml"
)
