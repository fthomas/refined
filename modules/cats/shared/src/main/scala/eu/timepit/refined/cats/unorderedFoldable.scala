package eu.timepit.refined.cats

import cats.UnorderedFoldable
import cats.syntax.unorderedFoldable._
import eu.timepit.refined.api.Validate
import eu.timepit.refined.collection.Size
import eu.timepit.refined.internal.Resources

/** Module for unorderedFoldable instances. */
object unorderedFoldable extends UnorderedFoldableInstances

trait UnorderedFoldableInstances {

  /**
   * `Validate` instance for `F` via `UnorderedFoldable[F]`.
   *
   * Examples: NonEmptyList, NonEmptyVector.
   */
  implicit def sizeValidateInstance[F[_]: UnorderedFoldable, T, P, RP](implicit
      v: Validate.Aux[Long, P, RP]
  ): Validate.Aux[F[T], Size[P], Size[v.Res]] =
    new Validate[F[T], Size[P]] {
      override type R = Size[v.Res]

      override def validate(t: F[T]): Res = {
        val r = v.validate(t.size)
        r.as(Size(r))
      }

      override def showExpr(t: F[T]): String =
        v.showExpr(t.size)

      override def showResult(t: F[T], r: Res): String = {
        val size = t.size
        val nested = v.showResult(size, r.detail.p)
        Resources.predicateTakingResultDetail(s"size($t) = $size", r, nested)
      }
    }
}
