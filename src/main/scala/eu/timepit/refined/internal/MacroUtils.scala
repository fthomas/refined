package eu.timepit.refined
package internal

import scala.reflect.macros.blackbox

object MacroUtils {
  def eval[T](c: blackbox.Context)(t: c.Expr[T]): T = {
    val expr = c.Expr[T](c.untypecheck(t.tree))

    // Try evaluating expr twice before failing, see
    // https://github.com/fthomas/refined/issues/3
    tryTwice(c.eval(expr))
  }

  def tryTwice[T](t: => T): T =
    scala.util.Try(t).getOrElse(t)
}
