package eu.timepit.refined
package macros

import eu.timepit.refined.api.{ RefType, Validate }
import eu.timepit.refined.internal.Resources
import macrocompat.bundle

import scala.reflect.macros.blackbox

@bundle
class RefineMacro(val c: blackbox.Context) extends MacroUtils {
  import c.universe._

  def impl[F[_, _], T: c.WeakTypeTag, P: c.WeakTypeTag](t: c.Expr[T])(
    v: c.Expr[Validate[T, P]], rt: c.Expr[RefType[F]]
  ): c.Expr[F[T, P]] = {

    val validate = eval(v)

    val tValue: T = t.tree match {
      case Literal(Constant(value)) => value.asInstanceOf[T]
      case _ if validate.isConstant => null.asInstanceOf[T]
      case _ => abort(Resources.refineNonCompileTimeConstant)
    }

    val res = validate.validate(tValue)
    if (res.isFailed) {
      abort(validate.showResult(tValue, res))
    }

    val refType = eval(rt)
    refType.unsafeWrapM(c)(t)
  }
}
