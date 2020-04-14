package eu.timepit.refined.macros

import eu.timepit.refined.api.{RefType, RefinedLT, Validate}
import eu.timepit.refined.char.{Digit, Letter, LowerCase, UpperCase, Whitespace}
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.internal.{Resources, WitnessAs}
import eu.timepit.refined.numeric.{Negative, NonNegative, NonPositive, Positive}
import scala.reflect.macros.blackbox

class RefineMacro(val c: blackbox.Context) extends MacroUtils with LiteralMatchers {

  import c.universe._

  def impl[F[_, _], T: c.WeakTypeTag, P: c.WeakTypeTag](t: c.Expr[T])(
      rt: c.Expr[RefType[F]],
      v: c.Expr[Validate[T, P]]
  ): c.Expr[F[T, P]] = {
    val tValue: T = t.tree match {
      case Literal(Constant(value)) => value.asInstanceOf[T]
      case BigDecimalMatcher(value) => value.asInstanceOf[T]
      case BigIntMatcher(value)     => value.asInstanceOf[T]
      case _                        => abort(Resources.refineNonCompileTimeConstant)
    }

    val validate = validateInstance(v)
    val res = validate.validate(tValue)
    if (res.isFailed) {
      abort(validate.showResult(tValue, res))
    }

    refTypeInstance(rt).unsafeWrapM(c)(t)
  }

  def implApplyRef[FTP, F[_, _], T: c.WeakTypeTag, P: c.WeakTypeTag](t: c.Expr[T])(
      ev: c.Expr[F[T, P] =:= FTP],
      rt: c.Expr[RefType[F]],
      v: c.Expr[Validate[T, P]]
  ): c.Expr[FTP] =
    c.Expr[FTP](impl(t)(rt, v).tree)

  def implRefLT[L: c.WeakTypeTag, T: c.WeakTypeTag, P: c.WeakTypeTag](
      w: c.Expr[WitnessAs[L, T]],
      v: c.Expr[Validate[T, P]]
  ): c.Expr[RefinedLT[L, P]] = {
    // doing eval(v) before eval(w) is important for... reasons.
    val validate = eval(v)
    val litval = w.tree match {
      case q"$_.WitnessAs.natWitnessAs[..$_]($_, new $_.Inst[..$_]($lv).asInstanceOf[..$_], $_)" => {
        // trying to eval the Nat witness directly confuses the typer, it is
        // tripping on some type recursion involving _5 and Succ[_4] in same expression
        eval(c.Expr[T](lv))
      }
      case _ => eval(w).snd
    }
    val res = validate.validate(litval)
    if (res.isFailed) {
      abort(validate.showResult(litval, res))
    }
    RefinedLT.manifest[L, T, P](c)(w, v)
  }

  private def validateInstance[T, P](v: c.Expr[Validate[T, P]])(
      implicit
      T: c.WeakTypeTag[T],
      P: c.WeakTypeTag[P]
  ): Validate[T, P] =
    validateInstances
      .collectFirst {
        case (tpeT, instancesForT) if tpeT =:= T.tpe =>
          instancesForT.collectFirst {
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
