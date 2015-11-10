package eu.timepit.refined
package internal

import scala.reflect.macros.blackbox
import scala.util.{ Success, Try }

trait MacroUtils {

  val c: blackbox.Context

  def eval[T](t: c.Expr[T]): T = {
    val expr = c.Expr[T](c.untypecheck(t.tree))

    // Try evaluating expr twice before failing, see
    // https://github.com/fthomas/refined/issues/3
    tryN(2, c.eval(expr))
  }

  def tryN[T](n: Int, t: => T): T =
    Stream.fill(n)(Try(t)).collectFirst { case Success(r) => r }.getOrElse(t)
}
