package eu.timepit.refined

import eu.timepit.refined.api.{ Inference, Validate }
import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.string._
import shapeless.Witness

/**
 * Module for `String` related predicates. Note that most of the predicates
 * in `[[collection]]` also work for `String`s by treating them as sequences
 * of `Char`s.
 */
object string extends StringValidate with StringInference {

  /** Predicate that checks if a `String` ends with the suffix `S`. */
  case class EndsWith[S](s: S)

  /** Predicate that checks if a `String` matches the regular expression `S`. */
  case class MatchesRegex[S](s: S)

  /** Predicate that checks if a `String` is a valid regular expression. */
  case class Regex()

  /** Predicate that checks if a `String` starts with the prefix `S`. */
  case class StartsWith[S](s: S)

  /** Predicate that checks if a `String` is a valid URI. */
  case class Uri()

  /** Predicate that checks if a `String` is a valid URL. */
  case class Url()

  /** Predicate that checks if a `String` is a valid UUID. */
  case class Uuid()

  /** Predicate that checks if a `String` is well-formed XML. */
  case class Xml()

  /** Predicate that checks if a `String` is a valid XPath expression. */
  case class XPath()
}

private[refined] trait StringValidate {

  implicit def endsWithValidate[S <: String](implicit ws: Witness.Aux[S]): Validate.Plain[String, EndsWith[S]] =
    Validate.fromPredicate(_.endsWith(ws.value), t => s""""$t".endsWith("${ws.value}")""", EndsWith(ws.value))

  implicit def matchesRegexValidate[S <: String](implicit ws: Witness.Aux[S]): Validate.Plain[String, MatchesRegex[S]] =
    Validate.fromPredicate(_.matches(ws.value), t => s""""$t".matches("${ws.value}")""", MatchesRegex(ws.value))

  implicit def regexValidate: Validate.Plain[String, Regex] =
    Validate.fromPartial(new scala.util.matching.Regex(_), "Regex", Regex())

  implicit def startsWithValidate[S <: String](implicit ws: Witness.Aux[S]): Validate.Plain[String, StartsWith[S]] =
    Validate.fromPredicate(_.startsWith(ws.value), t => s""""$t".startsWith("${ws.value}")""", StartsWith(ws.value))

  implicit def uriValidate: Validate.Plain[String, Uri] =
    Validate.fromPartial(new java.net.URI(_), "Uri", Uri())

  implicit def urlValidate: Validate.Plain[String, Url] =
    Validate.fromPartial(new java.net.URL(_), "Url", Url())

  implicit def uuidValidate: Validate.Plain[String, Uuid] =
    Validate.fromPartial(java.util.UUID.fromString, "Uuid", Uuid())

  implicit def xmlValidate: Validate.Plain[String, Xml] =
    Validate.fromPartial(scala.xml.XML.loadString, "Xml", Xml())

  implicit def xpathValidate: Validate.Plain[String, XPath] =
    Validate.fromPartial(javax.xml.xpath.XPathFactory.newInstance().newXPath().compile, "XPath", XPath())
}

private[refined] trait StringInference {

  implicit def endsWithInference[A <: String, B <: String](
    implicit
    wa: Witness.Aux[A], wb: Witness.Aux[B]
  ): EndsWith[A] ==> EndsWith[B] =
    Inference(wa.value.endsWith(wb.value), s"endsWithInference(${wa.value}, ${wb.value})")

  implicit def startsWithInference[A <: String, B <: String](
    implicit
    wa: Witness.Aux[A], wb: Witness.Aux[B]
  ): StartsWith[A] ==> StartsWith[B] =
    Inference(wa.value.startsWith(wb.value), s"startsWithInference(${wa.value}, ${wb.value})")
}
