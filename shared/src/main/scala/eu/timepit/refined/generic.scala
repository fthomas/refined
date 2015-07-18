package eu.timepit.refined

import eu.timepit.refined.InferenceRule.==>
import eu.timepit.refined.generic._
import shapeless.ops.coproduct.ToHList
import shapeless.ops.hlist.ToList
import shapeless.ops.record.Keys
import shapeless.{ Coproduct, HList, LabelledGeneric, Witness }

object generic extends GenericPredicates with GenericInferenceRules {

  /** Predicate that checks if a value is equal to `U`. */
  trait Equal[U]

  trait ConstructorNames[P]

  trait FieldNames[P]

  /** Predicate that witnesses that the type of a value is a subtype of `U`. */
  trait Subtype[U]

  /** Predicate that witnesses that the type of a value is a supertype of `U`. */
  trait Supertype[U]
}

private[refined] trait GenericPredicates {

  implicit def equalPredicate[T, U <: T](implicit wu: Witness.Aux[U]): Predicate[Equal[U], T] =
    Predicate.instance(_ == wu.value, t => s"($t == ${wu.value})")

  implicit def ctorNamesPredicate[T, P, R0 <: Coproduct, R1 <: HList, K <: HList](
    implicit
    lg: LabelledGeneric.Aux[T, R0],
    k: ToHList.Aux[R0, R1],
    keys: Keys.Aux[R1, K],
    ktl: ToList[K, Any],
    p: Predicate[P, List[String]]
  ): Predicate[ConstructorNames[P], T] = {

    val ctorNames = keys().toList.map(_.toString.drop(1))
    Predicate.constant(p.isValid(ctorNames), p.show(ctorNames))
  }

  implicit def fieldNamesPredicate[T, P, R <: HList, K <: HList](
    implicit
    lg: LabelledGeneric.Aux[T, R],
    keys: Keys.Aux[R, K],
    ktl: ToList[K, Any],
    p: Predicate[P, List[String]]
  ): Predicate[FieldNames[P], T] = {

    val fieldNames = keys().toList.map(_.toString.drop(1))
    Predicate.constant(p.isValid(fieldNames), p.show(fieldNames))
  }

  implicit def subtypePredicate[T, U >: T]: Predicate[Subtype[U], T] =
    Predicate.alwaysValid

  implicit def supertypePredicate[T, U <: T]: Predicate[Supertype[U], T] =
    Predicate.alwaysValid
}

private[refined] trait GenericInferenceRules {

  implicit def equalPredicateInference[T, U <: T, P](implicit p: Predicate[P, T], wu: Witness.Aux[U]): Equal[U] ==> P =
    InferenceRule(p.isValid(wu.value), s"equalPredicateInference(${p.show(wu.value)})")
}
