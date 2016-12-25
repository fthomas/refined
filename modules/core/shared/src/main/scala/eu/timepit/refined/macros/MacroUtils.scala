package eu.timepit.refined
package macros

import macrocompat.bundle
import scala.reflect.macros.blackbox
import scala.util.{Success, Try}

@bundle
trait MacroUtils {
  val c: blackbox.Context

  def abort(msg: String): Nothing =
    c.abort(c.enclosingPosition, msg)

  def eval[T](t: c.Expr[T]): T = {
    val expr = c.Expr[T](c.untypecheck(t.tree))

    // Try evaluating expr twice before failing, see
    // https://github.com/fthomas/refined/issues/3
    tryN(2, c.eval(expr))
  }

  def tryN[T](n: Int, t: => T): T =
    Stream.fill(n)(Try(t)).collectFirst { case Success(r) => r }.getOrElse(t)
}
