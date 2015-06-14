package eu.timepit.refined

import eu.timepit.refined.InferenceRuleAlias.==>
import eu.timepit.refined.string._
import shapeless.Witness

import scala.util.{ Failure, Success, Try }

object string extends StringPredicates with StringInferenceRules {

  /** Predicate that checks if a `String` ends with the suffix `S`. */
  trait EndsWith[S]

  /** Predicate that checks if a `String` matches the regular expression `R`. */
  trait MatchesRegex[R]

  /** Predicate that checks if a `String` is a valid regular expression. */
  trait Regex

  /** Predicate that checks if a `String` starts with the prefix `S`. */
  trait StartsWith[S]
}

trait StringPredicates {

  implicit def endsWithPredicate[R <: String](implicit wr: Witness.Aux[R]): Predicate[EndsWith[R], String] =
    Predicate.instance(_.endsWith(wr.value), t => s""""$t".endsWith("${wr.value}")""")

  implicit def matchesRegexPredicate[R <: String](implicit wr: Witness.Aux[R]): Predicate[MatchesRegex[R], String] =
    Predicate.instance(_.matches(wr.value), t => s""""$t".matches("${wr.value}")""")

  implicit val regexPredicate: Predicate[Regex, String] =
    new Predicate[Regex, String] {
      def isValid(t: String): Boolean = Try(t.r).isSuccess
      def show(t: String): String = s"""isValidRegex("$t")"""

      override def validated(t: String): Option[String] =
        Try(t.r) match {
          case Success(_) => None
          case Failure(ex) => Some(s"Predicate ${show(t)} failed: ${ex.getMessage}")
        }
    }

  implicit def startsWithPredicate[R <: String](implicit wr: Witness.Aux[R]): Predicate[StartsWith[R], String] =
    Predicate.instance(_.startsWith(wr.value), t => s""""$t".startsWith("${wr.value}")""")
}

trait StringInferenceRules {

  implicit def endsWithInference[A <: String, B <: String](implicit wa: Witness.Aux[A], wb: Witness.Aux[B]): EndsWith[A] ==> EndsWith[B] =
    InferenceRule(wa.value.endsWith(wb.value))

  implicit def startsWithInference[A <: String, B <: String](implicit wa: Witness.Aux[A], wb: Witness.Aux[B]): StartsWith[A] ==> StartsWith[B] =
    InferenceRule(wa.value.startsWith(wb.value))
}
