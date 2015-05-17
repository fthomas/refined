package eu.timepit.refined

import shapeless.Witness

object string {
  trait MatchesRegex[R]

  implicit def matchesRegexPredicate[R <: String](implicit wr: Witness.Aux[R]): Predicate[MatchesRegex[R], String] =
    Predicate.instance(_.matches(wr.value), _ => "")
}
