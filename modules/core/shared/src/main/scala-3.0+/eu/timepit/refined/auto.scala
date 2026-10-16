package eu.timepit.refined

import eu.timepit.refined.api.{RefType, Refined, Validate}
import eu.timepit.refined.internal.WitnessAs

import scala.quoted.{Expr, Quotes, ToExpr, Type, FromExpr}
import scala.compiletime.error
import scala.compiletime.{constValue, erasedValue}

/**
 * Module that provides automatic refinements and automatic conversions
 * between refined types (refinement subtyping) at compile-time.
 */
object auto {

  /**
   * Implicitly unwraps the `T` from a value of type `F[T, P]` using the
   * `[[api.RefType]]` instance of `F`. This allows a `F[T, P]` to be
   * used as it were a subtype of `T`.
   *
   * Example: {{{
   * scala> import eu.timepit.refined.auto.autoUnwrap
   *      | import eu.timepit.refined.types.numeric.PosInt
   *
   * scala> def plusOne(i: Int): Int = i + 1
   *      | val x = PosInt.unsafeFrom(42)
   *
   * // converts x implicitly to an Int:
   * scala> plusOne(x)
   * res0: Int = 43
   * }}}
   *
   * Note: This conversion is not needed if `F[T, _] <: T` holds (which
   * is the case for `shapeless.tag.@@`, for example).
   */
  implicit def autoUnwrap[F[_, _], T, P](tp: F[T, P])(implicit rt: RefType[F]): T =
    rt.unwrap(tp)

  inline def autoRefineV[P](inline t: Int)(implicit validate: Validate[Int, P]): Refined[Int, P] =
    ${ autoRefineVCode[P]('t, 'validate) }

  private def autoRefineVCode[P : Type](t: Expr[Int], validate: Expr[Validate[Int, P]])(using q: Quotes): Expr[Refined[Int, P]] =
    val tValue = t.valueOrError
    Type.of[P] match
      case '[ eu.timepit.refined.numeric.Greater[x] ] =>
        val tpe = q.reflect.TypeTree.of[P]

        val validateInstance = tpe.tpe match
          case appliedType: q.reflect.AppliedType =>
            appliedType.args.headOption match
              case Some(ct: q.reflect.ConstantType) =>
                ct.constant.value match
                  case limit: Int =>
                    numeric.Greater.greaterValidate(WitnessAs.apply(limit, limit), summon[Numeric[Int]])
                  case e =>
                    q.reflect.report.throwError(s"cannot match: $e")
              case e =>
                q.reflect.report.throwError(s"cannot match: $e")
          case a => q.reflect.report.throwError(s"not here:: $a")

        val res = validateInstance.validate(tValue)
        if (res.isFailed) {
          val msg = validateInstance.showResult(tValue, res)
          q.reflect.report.throwError(msg)
        }
        '{ RefType[Refined].unsafeWrap[Int, P]($t) }
      case _ =>
        q.reflect.report.throwError("wrong: " + Type.show[P])
}
