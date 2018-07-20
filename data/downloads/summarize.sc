import ammonite.ops._

def summarizeMonth(data: Vector[String]) =
  data.map(_.split(",").lift(1).map(_.replace("\"", "").toInt).getOrElse(0)).sum

val regex = """(\d{4})-(\d{2})-(.+)_(.+)\.csv""".r
val files = (ls! pwd).filter(p => regex.pattern.matcher(p.name).matches).toList

final case class Data(
  year: String,
  month: String,
  module: String,
  scalaVersion: String,
  totalDownloads: Int
)

val allData = files.map { p =>
  val regex(year, month, module, scalaVersion) = p.name
  val data = read.lines! p
  val totalDownloads = summarizeMonth(data)
  Data(year, month, module, scalaVersion, totalDownloads)
}

val grouped = allData
  .groupBy(d => (d.year, d.month, d.module))
  .mapValues(_.sortBy(_.scalaVersion).reverse)
  .toList
  .sortBy(_._1)
  .reverse

def formatMonth(year: String, month: String, module: String, aggregated: List[Data]): String = {
  val lines = aggregated.map(d => s"  ${d.scalaVersion}\t${d.totalDownloads}").mkString("\n")
  s"""$year-$month $module
     |$lines
     |  total\t${aggregated.map(_.totalDownloads).sum}
     |""".stripMargin
}

val out = grouped.map { case ((year, month, module), aggregated) =>
  formatMonth(year, month, module, aggregated)
}.mkString("", "\n", "\n")

write.over(pwd/"summary.txt" , out)
