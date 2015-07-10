package eu.timepit.refined

import eu.timepit.refined.InferenceRule.==>
import eu.timepit.refined.generic._
import shapeless.Witness

object generic extends GenericPredicates with GenericInferenceRules {

  /** Predicate that checks if a value is equal to `U`. */
  trait Equal[U]

  /** Predicate that witness that the type of a value is a subtype of `U`. */
  trait SubtypeOf[U]

  /** Predicate that witness that the type of a value is a supertype of `U`. */
  trait SupertypeOf[U]
}

private[refined] trait GenericPredicates {

  implicit def equalPredicate[T, U <: T](implicit wu: Witness.Aux[U]): Predicate[Equal[U], T] =
    Predicate.instance(_ == wu.value, t => s"($t == ${wu.value})")

  implicit def subtypeOfPredicate[T, U >: T]: Predicate[SubtypeOf[U], T] =
    Predicate.alwaysValid

  implicit def supertypeOfPredicate[T, U <: T]: Predicate[SupertypeOf[U], T] =
    Predicate.alwaysValid
}

private[refined] trait GenericInferenceRules {

  implicit def equalPredicateInference[T, U <: T, P](implicit p: Predicate[P, T], wu: Witness.Aux[U]): Equal[U] ==> P =
    InferenceRule(p.isValid(wu.value), s"equalPredicateInference(${p.show(wu.value)})")
}
