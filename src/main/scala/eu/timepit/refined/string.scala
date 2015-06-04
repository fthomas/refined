package eu.timepit.refined

import eu.timepit.refined.InferenceRuleAlias.==>
import eu.timepit.refined.internal.WeakWitness
import eu.timepit.refined.string._

object string extends StringPredicates with StringInferenceRules {

  /** Predicate that checks if a `String` ends with the suffix `S`. */
  trait EndsWith[S]

  /** Predicate that checks if a `String` matches the regular expression `R`. */
  trait MatchesRegex[R]

  /** Predicate that checks if a `String` starts with the prefix `S`. */
  trait StartsWith[S]
}

trait StringPredicates {

  implicit def endsWithPredicate[R <: String](implicit wr: WeakWitness.Aux[R]): Predicate[EndsWith[R], String] =
    Predicate.instance(_.endsWith(wr.value), t => s""""$t".endsWith("${wr.value}")""")

  implicit def matchesRegexPredicate[R <: String](implicit wr: WeakWitness.Aux[R]): Predicate[MatchesRegex[R], String] =
    Predicate.instance(_.matches(wr.value), t => s""""$t".matches("${wr.value}")""")

  implicit def startsWithPredicate[R <: String](implicit wr: WeakWitness.Aux[R]): Predicate[StartsWith[R], String] =
    Predicate.instance(_.startsWith(wr.value), t => s""""$t".startsWith("${wr.value}")""")
}

trait StringInferenceRules {

  implicit def endsWithInference[A <: String, B <: String](implicit wa: WeakWitness.Aux[A], wb: WeakWitness.Aux[B]): EndsWith[A] ==> EndsWith[B] =
    InferenceRule(wa.value.endsWith(wb.value))

  implicit def startsWithInference[A <: String, B <: String](implicit wa: WeakWitness.Aux[A], wb: WeakWitness.Aux[B]): StartsWith[A] ==> StartsWith[B] =
    InferenceRule(wa.value.startsWith(wb.value))
}
