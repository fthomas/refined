package eu.timepit.refined
package internal

import eu.timepit.refined.api.{ RefType, Validate }

import scala.reflect.macros.blackbox

object RefineM {

  def macroImpl[F[_, _], T: c.WeakTypeTag, P: c.WeakTypeTag](c: blackbox.Context)(t: c.Expr[T])(
    v: c.Expr[Validate[T, P]], rt: c.Expr[RefType[F]]
  ): c.Expr[F[T, P]] = {
    import c.universe._

    val validate = MacroUtils.eval(c)(v)

    val tValue: T = t.tree match {
      case Literal(Constant(value)) => value.asInstanceOf[T]
      case _ if validate.isConstant => null.asInstanceOf[T]
      case _ =>
        val msg = "compile-time refinement only works with literals or constant predicates"
        c.abort(c.enclosingPosition, msg)
    }

    val res = validate.validate(tValue)
    if (res.isFailed) {
      c.abort(c.enclosingPosition, validate.showResult(tValue, res))
    }

    val refType = MacroUtils.eval(c)(rt)
    refType.unsafeWrapM(c)(t)
  }
}
