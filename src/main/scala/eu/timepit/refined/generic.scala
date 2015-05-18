package eu.timepit.refined

import shapeless.Witness

object generic {
  /** Predicate that checks if a value is equal to `U`. */
  trait Equal[U]

  implicit def equalPredicate[T, U <: T](implicit wu: Witness.Aux[U]): Predicate[Equal[U], T] =
    Predicate.instance(_ == wu.value, t => s"($t == ${wu.value})")
}
