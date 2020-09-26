package eu.timepit.refined

import eu.timepit.refined.api.{Inference, Validate}
import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.generic._
import eu.timepit.refined.internal.WitnessAs

/** Module for generic predicates. */
object generic extends GenericInference {

  /** Predicate that checks if a value is equal to `U`. */
  final case class Equal[U](u: U)

  object Equal {
    implicit def equalValidate[T, U](implicit
        wu: WitnessAs[U, T]
    ): Validate.Plain[T, Equal[U]] =
      Validate.fromPredicate(_ == wu.snd, t => s"($t == ${wu.snd})", Equal(wu.fst))
  }
}

private[refined] trait GenericInference {

  implicit def equalValidateInference[T, U, P](implicit
      v: Validate[T, P],
      wu: WitnessAs[U, T]
  ): Equal[U] ==> P =
    Inference(v.isValid(wu.snd), s"equalValidateInference(${v.showExpr(wu.snd)})")
}
