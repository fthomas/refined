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

final case class RowData(
  year: String,
  month: String,
  module: String,
  downloadsTotal: Int,
  downloads_2_12: Option[Int],
  downloads_2_11: Option[Int],
  downloads_2_10: Option[Int]
)

val allData = files.map { p =>
  val regex(year, month, module, scalaVersion) = p.name
  val data = read.lines! p
  val totalDownloads = summarizeMonth(data)
  Data(year, month, module, scalaVersion, totalDownloads)
}

val allRowData = allData
  .groupBy(d => (d.year, d.month, d.module))
  .toList
  .sortBy(_._1)
  .reverse
  .map { case ((year, month, module), ds) =>
    RowData(
      year,
      month,
      module,
      ds.map(_.totalDownloads).sum,
      downloads_2_12 = ds.find(_.scalaVersion == "2.12").map(_.totalDownloads),
      downloads_2_11 = ds.find(_.scalaVersion == "2.11").map(_.totalDownloads),
      downloads_2_10 = ds.find(_.scalaVersion == "2.10").map(_.totalDownloads)
    )
  }

val out = allRowData.map { row =>
  s""""${row.year}-${row.month}","${row.module}","${row.downloadsTotal}"""" +
    s""","${row.downloads_2_12.fold("")(_.toString)}"""" +
    s""","${row.downloads_2_11.fold("")(_.toString)}"""" +
    s""","${row.downloads_2_10.fold("")(_.toString)}""""
}.mkString("", "\n", "\n")

write.over(pwd/"summary.csv" , out)
