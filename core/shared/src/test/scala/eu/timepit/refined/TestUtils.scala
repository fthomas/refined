package eu.timepit.refined

import eu.timepit.refined.api.Validate
import org.scalacheck.Prop

object TestUtils {

  def isValid[P]: IsValidPartiallyApplied[P] = new IsValidPartiallyApplied

  class IsValidPartiallyApplied[P] {
    def apply[T](t: T)(implicit v: Validate[T, P]): Boolean = v.isValid(t)
  }

  def notValid[P]: NotValidPartiallyApplied[P] = new NotValidPartiallyApplied

  class NotValidPartiallyApplied[P] {
    def apply[T](t: T)(implicit v: Validate[T, P]): Boolean = v.notValid(t)
  }

  def validate[P]: ValidatePartiallyApplied[P] = new ValidatePartiallyApplied

  class ValidatePartiallyApplied[P] {
    def apply[T](t: T)(implicit v: Validate[T, P]): v.Res = v.validate(t)
  }

  def showExpr[P]: ShowExprPartiallyApplied[P] = new ShowExprPartiallyApplied

  class ShowExprPartiallyApplied[P] {
    def apply[T](t: T)(implicit v: Validate[T, P]): String = v.showExpr(t)
  }

  def showResult[P]: ShowResultPartiallyApplied[P] = new ShowResultPartiallyApplied

  class ShowResultPartiallyApplied[P] {
    def apply[T](t: T)(implicit v: Validate[T, P]): String = v.showResult(t, v.validate(t))
  }

  def wellTyped(body: => Unit): Prop = Prop.secure {
    body
    true
  }

  def getClassFile[C](c: C): String =
    c.getClass.getCanonicalName.replace('.', '/') + ".class"

  def getClassFilePath[C](c: C): java.net.URL =
    getClass.getClassLoader.getResource(getClassFile(c))

  def javapOutput[C](c: C, opts: String = ""): String =
    scala.sys.process
      .Process(s"javap $opts ${getClassFilePath(c)}")
      .!!
      .trim
      .replaceAll("""(?m)\s+$""", "")
}
