package eu.timepit.refined
package internal

import scala.reflect.macros.blackbox
import scala.util.{ Success, Try }

object MacroUtils {
  def eval[T](c: blackbox.Context)(t: c.Expr[T]): T = {
    val expr = c.Expr[T](c.untypecheck(t.tree))

    // Try evaluating expr twice before failing, see
    // https://github.com/fthomas/refined/issues/3
    tryN(2, c.eval(expr))
  }

  def tryN[T](n: Int, t: => T): T =
    Stream.fill(n)(Try(t)).collect { case Success(r) => r }.headOption.getOrElse(t)
}
