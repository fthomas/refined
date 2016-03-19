package eu.timepit.refined
package macros

import eu.timepit.refined.api.{ RefType, TypedInference }
import eu.timepit.refined.internal.Resources
import macrocompat.bundle
import scala.reflect.macros.blackbox

@bundle
class InferMacro(val c: blackbox.Context) extends MacroUtils {
  import c.universe._

  def impl[F[_, _], T, A: c.WeakTypeTag, B: c.WeakTypeTag](ta: c.Expr[F[T, A]])(
    rt: c.Expr[RefType[F]], ti: c.Expr[TypedInference[T, A, B]]
  ): c.Expr[F[T, B]] = {

    val inference = eval(ti)
    if (inference.notValid) {
      abort(Resources.invalidInference(weakTypeOf[A].toString, weakTypeOf[B].toString))
    }
    reify(rt.splice.unsafeRewrap(ta.splice))
  }
}
