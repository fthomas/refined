package eu.timepit.refined
package macros

import eu.timepit.refined.api.{RefType, Validate}
import eu.timepit.refined.char.{Digit, Letter, LowerCase, UpperCase, Whitespace}
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.internal.Resources
import eu.timepit.refined.numeric.{Negative, NonNegative, NonPositive, Positive}
import macrocompat.bundle
import scala.reflect.macros.blackbox

@bundle
class RefineMacro(val c: blackbox.Context) extends MacroUtils {
  import c.universe._

  def impl[F[_, _], T: c.WeakTypeTag, P: c.WeakTypeTag](t: c.Expr[T])(
      rt: c.Expr[RefType[F]],
      v: c.Expr[Validate[T, P]]
  ): c.Expr[F[T, P]] = {

    val tValue: T = t.tree match {
      case Literal(Constant(value)) => value.asInstanceOf[T]
      case _                        => abort(Resources.refineNonCompileTimeConstant)
    }

    val validate = validateInstance(v)
    val res = validate.validate(tValue)
    if (res.isFailed) {
      abort(validate.showResult(tValue, res))
    }

    refTypeInstance(rt).unsafeWrapM(c)(t)
  }

  def implApplyRef[FTP, F[_, _], T, P](t: c.Expr[T])(
      ev: c.Expr[F[T, P] =:= FTP],
      rt: c.Expr[RefType[F]],
      v: c.Expr[Validate[T, P]]
  ): c.Expr[FTP] =
    c.Expr(impl(t)(rt, v).tree)

  private def validateInstance[T, P](v: c.Expr[Validate[T, P]])(
      implicit
      T: c.WeakTypeTag[T],
      P: c.WeakTypeTag[P]
  ): Validate[T, P] =
    validateInstances
      .collectFirst {
        case (tpeT, list) if tpeT =:= T.tpe =>
          list.collectFirst {
            case (tpeP, validate) if tpeP =:= P.tpe =>
              validate.asInstanceOf[Validate[T, P]]
          }
      }
      .flatten
      .getOrElse(eval(v))

  private val validateInstances: List[(Type, List[(Type, Any)])] = {
    def instance[T, P](implicit P: c.WeakTypeTag[P], v: Validate[T, P]): (Type, Validate[T, P]) =
      P.tpe -> v

    List(
      weakTypeOf[Int] -> List(
        instance[Int, Positive],
        instance[Int, NonPositive],
        instance[Int, Negative],
        instance[Int, NonNegative]
      ),
      weakTypeOf[Long] -> List(
        instance[Long, Positive],
        instance[Long, NonPositive],
        instance[Long, Negative],
        instance[Long, NonNegative]
      ),
      weakTypeOf[Double] -> List(
        instance[Double, Positive],
        instance[Double, NonPositive],
        instance[Double, Negative],
        instance[Double, NonNegative]
      ),
      weakTypeOf[String] -> List(
        instance[String, NonEmpty]
      ),
      weakTypeOf[Char] -> List(
        instance[Char, Digit],
        instance[Char, Letter],
        instance[Char, LowerCase],
        instance[Char, UpperCase],
        instance[Char, Whitespace]
      )
    )
  }
}
