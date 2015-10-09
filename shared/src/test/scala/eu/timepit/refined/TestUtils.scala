package eu.timepit.refined

import eu.timepit.refined.api.Validate
import org.scalacheck.Prop

object TestUtils {

  def isValid[P]: IsValidAux[P] = new IsValidAux

  class IsValidAux[P] {
    def apply[T](t: T)(implicit v: Validate[T, P]): Boolean = v.isValid(t)
  }

  def notValid[P]: NotValidAux[P] = new NotValidAux

  class NotValidAux[P] {
    def apply[T](t: T)(implicit v: Validate[T, P]): Boolean = v.notValid(t)
  }

  def validate[P]: ValidateAux[P] = new ValidateAux

  class ValidateAux[P] {
    def apply[T](t: T)(implicit v: Validate[T, P]): v.Res = v.validate(t)
  }

  def showExpr[P]: ShowExprAux[P] = new ShowExprAux

  class ShowExprAux[P] {
    def apply[T](t: T)(implicit v: Validate[T, P]): String = v.showExpr(t)
  }

  def showResult[P]: ShowResultAux[P] = new ShowResultAux

  class ShowResultAux[P] {
    def apply[T](t: T)(implicit v: Validate[T, P]): String = v.showResult(t, v.validate(t))
  }

  def wellTyped(body: => Unit): Prop = Prop.secure {
    body
    true
  }
}
