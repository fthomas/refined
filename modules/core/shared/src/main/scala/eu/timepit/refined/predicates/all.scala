package eu.timepit.refined.predicates

object all extends AllPredicates with AllPredicatesBinCompat1 with AllPredicatesBinCompat2

trait AllPredicates
    extends BooleanPredicates
    with CharPredicates
    with CollectionPredicates
    with GenericPredicates
    with NumericPredicates
    with StringPredicates

trait AllPredicatesBinCompat1 extends StringPredicatesBinCompat1

trait AllPredicatesBinCompat2 extends ImpurePredicates
