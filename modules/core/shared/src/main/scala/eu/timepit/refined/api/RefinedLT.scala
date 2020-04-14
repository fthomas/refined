package eu.timepit.refined.api

import scala.reflect.macros.blackbox
import eu.timepit.refined.macros.RefineMacro
import eu.timepit.refined.internal._
import shapeless.Nat

/**
 * Similar to `Refined[T, P]`, except manifests implicitly,
 * if and only if the value of literal type `L` satisfies `P` at
 * compile time.
 */
case class RefinedLT[L, P] private (expr: String)

trait RefinedLTP1 {
  implicit def refinedLTNat[L <: Nat, P](
      implicit
      w: WitnessAs[L, Int],
      v: Validate[Int, P]
  ): RefinedLT[L, P] =
    macro RefineMacro.implRefLT[L, Int, P]
}

object RefinedLT extends RefinedLTP1 {
  def manifest[L: c.WeakTypeTag, T: c.WeakTypeTag, P: c.WeakTypeTag](
      c: blackbox.Context
  )(w: c.Expr[WitnessAs[L, T]], v: c.Expr[Validate[T, P]]): c.Expr[RefinedLT[L, P]] =
    c.universe.reify(RefinedLT[L, P](v.splice.showExpr(w.splice.snd)))

  implicit def refinedLT[L, T, P](
      implicit
      w: WitnessAs[L, T],
      v: Validate[T, P]
  ): RefinedLT[L, P] =
    macro RefineMacro.implRefLT[L, T, P]
}
