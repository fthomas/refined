package eu.timepit.refined

import eu.timepit.refined.InferenceRule.==>
import eu.timepit.refined.generic._
import shapeless.Witness

object generic extends GenericPredicates with GenericInferenceRules {

  /** Predicate that checks if a value is equal to `U`. */
  trait Equal[U]

  /** Predicate that witnesses that the type of a value is a subtype of `U`. */
  trait Subtype[U]

  /** Predicate that witnesses that the type of a value is a supertype of `U`. */
  trait Supertype[U]
}

private[refined] trait GenericPredicates {

  implicit def equalPredicate[T, U <: T](implicit wu: Witness.Aux[U]): Predicate[Equal[U], T] =
    Predicate.instance(_ == wu.value, t => s"($t == ${wu.value})")

  implicit def subtypePredicate[T, U >: T]: Predicate[Subtype[U], T] =
    Predicate.alwaysValid

  implicit def supertypePredicate[T, U <: T]: Predicate[Supertype[U], T] =
    Predicate.alwaysValid
}

private[refined] trait GenericInferenceRules {

  implicit def equalPredicateInference[T, U <: T, P](implicit p: Predicate[P, T], wu: Witness.Aux[U]): Equal[U] ==> P =
    InferenceRule(p.isValid(wu.value), s"equalPredicateInference(${p.show(wu.value)})")
}
