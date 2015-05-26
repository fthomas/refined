package eu.timepit.refined

import eu.timepit.refined.boolean.Not
import shapeless.Witness

object generic {
  /** Predicate that checks if a value is equal to `U`. */
  trait Equal[U]

  /** Predicate that checks if a value is null. */
  trait IsNull

  /** Predicate that checks if a value is not null. */
  type NonNull = Not[IsNull]

  implicit def equalPredicate[T, U <: T](implicit wu: Witness.Aux[U]): Predicate[Equal[U], T] =
    Predicate.instance(_ == wu.value, t => s"($t == ${wu.value})")

  implicit def isNullPredicate[T]: Predicate[IsNull, T] =
    Predicate.instance(_ == null, t => s"($t == null)")
}
