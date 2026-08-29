package eu.timepit.refined.macros

import hearth.*
import eu.timepit.refined.api.{Inference, Refined, Validate}

import scala.quoted.*

private[refined] class RefinedMacros(q: Quotes) extends MacroCommonsScala3(using q), RefinedMacro

private[refined] object Macros {

  def autoRefineV[T: Type, P: Type](
      t: Expr[T],
      v: Expr[Validate[T, P]]
  )(using q: Quotes): Expr[Refined[T, P]] =
    new RefinedMacros(q).autoRefineImpl[T, P](t, v)

  def autoInfer[T: Type, A: Type, B: Type](
      ta: Expr[Refined[T, A]],
      ir: Expr[Inference[A, B]]
  )(using q: Quotes): Expr[Refined[T, B]] =
    new RefinedMacros(q).autoInferImpl[T, A, B](ta, ir)

  def refineMV[T: Type, P: Type](
      t: Expr[T],
      v: Expr[Validate[T, P]]
  )(using q: Quotes): Expr[Refined[T, P]] =
    new RefinedMacros(q).refineMVImpl[T, P](t, v)

  def refineM[T: Type, P: Type](
      t: Expr[T],
      v: Expr[Validate[T, P]]
  )(using q: Quotes): Expr[T] =
    new RefinedMacros(q).refineMImpl[T, P](t, v)

  def applyRef[FTP: Type, T: Type, P: Type](
      t: Expr[T],
      v: Expr[Validate[T, P]]
  )(using q: Quotes): Expr[FTP] =
    new RefinedMacros(q).applyRefImpl[FTP, T, P](t, v)
}
