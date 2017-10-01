package eu.timepit.refined.benchmark

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.refineV
import eu.timepit.refined.string.Regex
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Mode, OutputTimeUnit}

@BenchmarkMode(Array(Mode.AverageTime))
class RefineVBenchmark {

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  def refineV_Positive: Either[String, Int Refined Positive] =
    refineV[Positive](1)

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  def refineV_Regex: Either[String, String Refined Regex] =
    refineV[Regex](".*")
}
