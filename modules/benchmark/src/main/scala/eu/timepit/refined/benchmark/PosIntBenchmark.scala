package eu.timepit.refined.benchmark

import eu.timepit.refined.types.numeric.PosInt
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Mode, OutputTimeUnit}

@BenchmarkMode(Array(Mode.AverageTime))
class PosIntBenchmark {

  @Benchmark
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  def unsafeFrom_1: Any =
    PosInt.unsafeFrom(1)
}
