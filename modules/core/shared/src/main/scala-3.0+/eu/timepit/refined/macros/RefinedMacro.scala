package eu.timepit.refined.macros

import hearth.*
import eu.timepit.refined.api.{Inference, RefType, Refined, Validate}

trait RefinedMacro { this: MacroCommons =>

  def autoRefineImpl[T: Type, P: Type](
      t: Expr[T],
      v: Expr[Validate[T, P]]
  ): Expr[Refined[T, P]] = {
    validateAtCompileTime(t, v)
    Expr.quote(Refined.unsafeApply[T, P](Expr.splice(t)))
  }

  def autoInferImpl[T: Type, A: Type, B: Type](
      ta: Expr[Refined[T, A]],
      ir: Expr[Inference[A, B]]
  ): Expr[Refined[T, B]] = {
    val inference = ir.semiEval match {
      case Right(value) => value
      case Left(errors) =>
        Environment.reportErrorAndAbort(
          s"Cannot evaluate Inference[${Type[A].plainPrint}, ${Type[B].plainPrint}] at compile time: ${errors.mkString(", ")}. "
        )
    }
    if (!inference.isValid)
      Environment.reportErrorAndAbort(
        s"Inference failed: ${inference.show}"
      )
    Expr.quote(Refined.unsafeApply[T, B](Expr.splice(ta).value))
  }

  def refineMVImpl[T: Type, P: Type](
      t: Expr[T],
      v: Expr[Validate[T, P]]
  ): Expr[Refined[T, P]] = autoRefineImpl[T, P](t, v)

  /**
   * Validates `t` against `P` at compile time and returns `t` unchanged. Used by the carrier-generic
   * `RefType.refineM`, whose wrapping into `F[T, P]` is done by the (zero-cost) runtime `unsafeWrap`,
   * so no higher-kinded macro over `F` is needed.
   */
  def refineMImpl[T: Type, P: Type](
      t: Expr[T],
      v: Expr[Validate[T, P]]
  ): Expr[T] = {
    validateAtCompileTime(t, v)
    t
  }

  def applyRefImpl[FTP: Type, T: Type, P: Type](
      t: Expr[T],
      v: Expr[Validate[T, P]]
  ): Expr[FTP] = {
    validateAtCompileTime(t, v)
    val refined: Expr[Refined[T, P]] = Expr.quote(Refined.unsafeApply[T, P](Expr.splice(t)))
    Expr.upcast[Refined[T, P], FTP](refined)(using Type.of[Refined[T, P]], Type[FTP])
  }

  private def validateAtCompileTime[T: Type, P: Type](
      t: Expr[T],
      v: Expr[Validate[T, P]]
  ): Unit = {
    val tValue = t.semiEval match {
      case Right(value) => value
      case Left(errors) =>
        Environment.reportErrorAndAbort(
          s"Cannot evaluate expression at compile time: ${errors.mkString(", ")}"
        )
    }
    val validate = v.semiEval match {
      case Right(value) => value
      case Left(errors) =>
        Environment.reportErrorAndAbort(
          s"Cannot evaluate Validate[${Type[T].plainPrint}, ${Type[P].plainPrint}] at compile time: ${errors.mkString(", ")}. " +
            s"Use refineV for runtime validation instead."
        )
    }
    val result = validate.validate(tValue)
    if (!result.isPassed)
      Environment.reportErrorAndAbort(s"Predicate failed: ${validate.showResult(tValue, result)}")
  }
}
