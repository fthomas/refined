package eu.timepit.refined.benchmark

import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.refineV
import eu.timepit.refined.types.numeric.PosInt
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Mode, OutputTimeUnit}

@BenchmarkMode(Array(Mode.AverageTime))
class RefineVBenchmark {

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  def refineV_Positive: Either[String, PosInt] =
    refineV[Positive](1)
}
