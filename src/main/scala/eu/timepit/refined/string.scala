package eu.timepit.refined

import eu.timepit.refined.internal.WeakWitness
import eu.timepit.refined.string._

object string extends StringPredicates {

  /** Predicate that checks if a `String` matches the regular expression `R`. */
  trait MatchesRegex[R]
}

trait StringPredicates {

  implicit def matchesRegexPredicate[R <: String](implicit wr: WeakWitness.Aux[R]): Predicate[MatchesRegex[R], String] =
    Predicate.instance(_.matches(wr.value), t => s""""$t".matches("${wr.value}")""")
}
