package eu.timepit.refined.predicates

object all extends AllPredicates

trait AllPredicates
    extends BooleanPredicates
    with CharPredicates
    with CollectionPredicates
    with GenericPredicates
    with NumericPredicates
    with StringPredicates
