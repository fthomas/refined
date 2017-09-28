package eu.timepit.refined.benchmark

import org.openjdk.jmh.annotations._
import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox

@BenchmarkMode(Array(Mode.AverageTime))
@State(Scope.Thread)
class InferMacroBenchmark {
  private val toolBox =
    currentMirror.mkToolBox()

  private val autoInfer_Greater_tree =
    toolBox.parse("""
      import eu.timepit.refined.W
      import eu.timepit.refined.api.Refined
      import eu.timepit.refined.auto.autoInfer
      import eu.timepit.refined.numeric.Greater
      val a: Int Refined Greater[W.`5`.T] = Refined.unsafeApply(10)
      val b: Int Refined Greater[W.`0`.T] = a
      """)

  @Benchmark
  def autoInfer_Greater: Any =
    toolBox.eval(autoInfer_Greater_tree)
}
