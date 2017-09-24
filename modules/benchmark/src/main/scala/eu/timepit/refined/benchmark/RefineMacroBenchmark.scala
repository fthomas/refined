package eu.timepit.refined.benchmark

import org.openjdk.jmh.annotations.{Benchmark, Scope, State}
import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox

@State(Scope.Thread)
class RefineMacroBenchmark {
  private val toolBox =
    currentMirror.mkToolBox()

  private val autoRefineV_PosInt_tree =
    toolBox.parse("""
      import eu.timepit.refined.auto.autoRefineV
      import eu.timepit.refined.types.numeric.PosInt
      val x: PosInt = 1
      """)

  @Benchmark
  def autoRefineV_PosInt: Any =
    toolBox.eval(autoRefineV_PosInt_tree)
}
