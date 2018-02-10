package eu.timepit.refined

import eu.timepit.refined.api.Validate
import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox
import shapeless.Witness

object eval {

  /** Predicate that checks if a value applied to the predicate `S` yields `true`. */
  final case class Eval[S](s: S)

  object Eval {
    // Cache ToolBox for Eval Validate instances
    private lazy val toolBox = currentMirror.mkToolBox()

    implicit def evalValidate[T, S <: String](
        implicit mt: Manifest[T],
        ws: Witness.Aux[S]
    ): Validate.Plain[T, Eval[S]] = {
      // The ascription (T => Boolean) allows to omit the parameter
      // type in ws.value (i.e. "x => ..." instead of "(x: T) => ...").
      val tree = toolBox.parse(s"(${ws.value}): ($mt => Boolean)")

      val predicate = toolBox.eval(tree).asInstanceOf[T => Boolean]
      Validate.fromPredicate(predicate, _ => ws.value, Eval(ws.value))
    }
  }
}
