package eu.timepit.refined.api

/**
 * Evidence that states if the conclusion `C` can be inferred from the
 * premise `P` or not.
 *
 * This type class is used to implement refinement subtyping. If a valid
 * `Inference[P, C]` exists, the type `F[T, P]` is considered a subtype
 * of `F[T, C]`.
 */
case class Inference[P, C](isValid: Boolean, show: String) {

  final def adapt[P2, C2](adaptedShow: String): Inference[P2, C2] =
    copy(show = adaptedShow.format(show))

  final def notValid: Boolean =
    !isValid
}

object Inference {

  type ==>[P, C] = Inference[P, C]

  def apply[P, C](implicit i: Inference[P, C]): Inference[P, C] = i

  def alwaysValid[P, C](show: String): Inference[P, C] =
    Inference(isValid = true, show)

  def combine[P1, P2, P, C1, C2, C](
      i1: Inference[P1, C1],
      i2: Inference[P2, C2],
      show: String
  ): Inference[P, C] =
    Inference(i1.isValid && i2.isValid, show.format(i1.show, i2.show))
}

/**
 * Similar to `Inference[P, C]` but will not implicitly manifest if `C` cannot be
 * inferred from `P`.
 *
 * It is intended to be used with chained implicit definitions that require proof that `P ==> C`
 */
case class Implies[P, C](show: String)

object Implies {
  import scala.reflect.macros.blackbox
  import eu.timepit.refined.macros.InferMacro
  import Inference.==>

  implicit def autoImply[A, B](
      implicit
      ir: A ==> B
  ): Implies[A, B] = macro InferMacro.implies[A, B]

  def manifest[A: c.WeakTypeTag, B: c.WeakTypeTag](
      c: blackbox.Context
  )(ir: c.Expr[A ==> B]): c.Expr[Implies[A, B]] =
    c.universe.reify(Implies[A, B]((ir.splice).show))
}
