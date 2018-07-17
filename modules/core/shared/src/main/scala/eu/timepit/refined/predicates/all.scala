package eu.timepit.refined.predicates

object all extends AllPredicates with AllPredicatesBinCompat1

trait AllPredicates
    extends BooleanPredicates
    with CharPredicates
    with CollectionPredicates
    with GenericPredicates
    with ImpurePredicates
    with NumericPredicates
    with StringPredicates

trait AllPredicatesBinCompat1 extends StringPredicatesBinCompat1
