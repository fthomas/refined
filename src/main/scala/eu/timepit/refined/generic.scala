package eu.timepit.refined

import eu.timepit.refined.InferenceRuleAlias.==>
import eu.timepit.refined.boolean.Not
import eu.timepit.refined.generic._
import eu.timepit.refined.internal.WeakWitness

object generic extends GenericPredicates with GenericInferenceRules {

  /** Predicate that checks if a value is equal to `U`. */
  trait Equal[U]

  /** Predicate that checks if a value is `null`. */
  trait IsNull

  /** Predicate that checks if a value is not `null`. */
  type NonNull = Not[IsNull]
}

trait GenericPredicates {

  implicit def equalPredicate[T, U <: T](implicit wu: WeakWitness.Aux[U]): Predicate[Equal[U], T] =
    Predicate.instance(_ == wu.value, t => s"($t == ${wu.value})")

  implicit def isNullPredicate[T <: AnyRef]: Predicate[IsNull, T] =
    Predicate.instance(_ == null, t => s"($t == null)")
}

trait GenericInferenceRules {

  implicit def equalPredicateInference[T, U <: T, P](implicit p: Predicate[P, T], wu: WeakWitness.Aux[U]): Equal[U] ==> P =
    InferenceRule(p.isValid(wu.value))
}
