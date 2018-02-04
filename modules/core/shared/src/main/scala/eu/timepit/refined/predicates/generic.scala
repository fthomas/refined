package eu.timepit.refined.predicates

import eu.timepit.refined

object generic extends GenericPredicates

trait GenericPredicates {
  final type Equal[U] = refined.generic.Equal[U]
  final val Equal = refined.generic.Equal
}
