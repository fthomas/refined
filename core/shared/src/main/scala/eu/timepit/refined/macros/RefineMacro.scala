package eu.timepit.refined
package macros

import eu.timepit.refined.api.{ RefType, Validate }
import eu.timepit.refined.internal.Resources
import macrocompat.bundle
import scala.reflect.macros.blackbox

@bundle
class RefineMacro(val c: blackbox.Context) extends MacroUtils {
  import c.universe._

  def refineImpl[FTP, T: c.WeakTypeTag, P: c.WeakTypeTag](t: c.Expr[T])(
    rt: c.Expr[api.RefinedType.AuxT[FTP, T]]
  ): c.Expr[FTP] = {
    val tValue: T = t.tree match {
      case Literal(Constant(value)) => value.asInstanceOf[T]
      case _ => abort(Resources.refineNonCompileTimeConstant)
    }

    val refinedType = eval(rt)
    val res = refinedType.validate.validate(tValue)
    if (res.isFailed) {
      abort(refinedType.validate.showResult(tValue, res))
    }

    c.Expr[FTP](refinedType.refType.unsafeWrapM[T, P](c)(t).tree)
  }

  @deprecated("", "")
  def impl[F[_, _], T: c.WeakTypeTag, P: c.WeakTypeTag](t: c.Expr[T])(
    rt: c.Expr[RefType[F]], v: c.Expr[Validate[T, P]]
  ): c.Expr[F[T, P]] =
    refineImpl(t)(reify(api.RefinedType.instance(rt.splice, v.splice)))

  @deprecated("", "")
  def implApplyRef[FTP, F[_, _], T, P](t: c.Expr[T])(
    ev: c.Expr[F[T, P] =:= FTP], rt: c.Expr[RefType[F]], v: c.Expr[Validate[T, P]]
  ): c.Expr[FTP] =
    c.Expr(impl(t)(rt, v).tree)
}
