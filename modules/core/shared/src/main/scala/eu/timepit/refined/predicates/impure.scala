package eu.timepit.refined.predicates

import eu.timepit.refined

object impure extends ImpurePredicates

trait ImpurePredicates {
  final type NonNull[U] = refined.impure.NonNull[U]
  final val NonNull = refined.impure.NonNull
}
