package eu.timepit.refined

import shapeless.Witness

object string {
  /** Predicate that checks if a `String` matches the regular expression `R`. */
  trait MatchesRegex[R]

  implicit def matchesRegexPredicate[R <: String](implicit wr: Witness.Aux[R]): Predicate[MatchesRegex[R], String] =
    Predicate.instance(_.matches(wr.value), t => s""""$t".matches("${wr.value}")""")
}
