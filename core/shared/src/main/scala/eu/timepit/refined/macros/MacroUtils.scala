package eu.timepit.refined
package macros

import macrocompat.bundle
import scala.reflect.macros.blackbox

@bundle
trait MacroUtils {
  val c: blackbox.Context

  def abort(msg: String): Nothing =
    c.abort(c.enclosingPosition, msg)

  def eval[T](t: c.Expr[T]): T = {
    val expr = c.Expr[T](c.untypecheck(t.tree))
    c.eval(expr)
  }
}
