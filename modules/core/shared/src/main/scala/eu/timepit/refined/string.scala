package eu.timepit.refined

import eu.timepit.refined.api.{Inference, Validate}
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
  final case class EndsWith[S](s: S)

  /** Predicate that checks if a `String` is a valid IPv4 */
  final case class IPv4()
  object IPv4 {
    val regex = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$".r.pattern
    val maxOctet = 255
    val predicate: String => Boolean = s => {
      val matcher = regex.matcher(s)
      matcher.find() && matcher.matches() && {
        val octet1 = matcher.group(1).toInt
        val octet2 = matcher.group(2).toInt
        val octet3 = matcher.group(3).toInt
        val octet4 = matcher.group(4).toInt

        (octet1 <= maxOctet) && (octet2 <= maxOctet) && (octet3 <= maxOctet) && (octet4 <= maxOctet)
      }
    }
  }

  /** Predicate that checks if a `String` is a valid IPv6 */
  final case class IPv6()
  object IPv6 {
    val ipv4Chars = "(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])"
    val ipv4 = s"(${ipv4Chars}\\.){3,3}${ipv4Chars}"
    val ipv6Chars = "[0-9a-fA-F]{1,4}"

    val ipv6Full = s"(${ipv6Chars}:){7,7}${ipv6Chars}" // 1:2:3:4:5:6:7:8

    val ipv6Compact = List(
      s"(${ipv6Chars}:){1,7}:", // 1:: .. 1:2:3:4:5:6:7::
      s"(${ipv6Chars}:){1,6}:${ipv6Chars}", // 1::8 .. 1:2:3:4:5:6::8
      s"(${ipv6Chars}:){1,5}(:${ipv6Chars}){1,2}", // 1::7:8 .. 1:2:3:4:5::8
      s"(${ipv6Chars}:){1,4}(:${ipv6Chars}){1,3}", // 1::6:7:8 .. 1:2:3:4::8
      s"(${ipv6Chars}:){1,3}(:${ipv6Chars}){1,4}", // 1::5:6:7:8 .. 1:2:3::8
      s"(${ipv6Chars}:){1,2}(:${ipv6Chars}){1,5}", // 1::4:5:6:7:8 .. 1:2::8
      s"(${ipv6Chars}:)(:${ipv6Chars}){1,6}", // 1::3:4:5:6:7:8 .. 1::8
      s":((:${ipv6Chars}){1,7}|:)" // ::2:3:4:5:6:7:8 .. ::
    )

    val interface = "[0-9a-zA-Z]{1,}"
    val ipv6LinkLocal = s"fe80:(:${ipv6Chars}){0,4}%${interface}"

    val mappedIpv6 = s"::(ffff(:0{1,4}){0,1}:){0,1}${ipv4}"

    val embeddedIpv4 = s"(${ipv6Chars}:){1,4}:${ipv4}"

    val formats = List(
      ipv6Full,
      ipv6LinkLocal,
      mappedIpv6,
      embeddedIpv4
    ) ++ ipv6Compact

    val regex = formats.map(regex => s"(^${regex}$$)").mkString("|").r.pattern
    val predicate: String => Boolean = s => regex.matcher(s).matches
  }

  /** Predicate that checks if a `String` matches the regular expression `S`. */
  final case class MatchesRegex[S](s: S)

  /** Predicate that checks if a `String` is a valid regular expression. */
  final case class Regex()

  /** Predicate that checks if a `String` starts with the prefix `S`. */
  final case class StartsWith[S](s: S)

  /** Predicate that checks if a `String` is a valid URI. */
  final case class Uri()

  /** Predicate that checks if a `String` is a valid URL. */
  final case class Url()

  /** Predicate that checks if a `String` is a valid UUID. */
  final case class Uuid()

  /** Predicate that checks if a `String` is a parsable `Int`. */
  final case class ValidInt()

  /** Predicate that checks if a `String` is a parsable `Long`. */
  final case class ValidLong()

  /** Predicate that checks if a `String` is a parsable `Double`. */
  final case class ValidDouble()

  /** Predicate that checks if a `String` is a parsable `BigInt`. */
  final case class ValidBigInt()

  /** Predicate that checks if a `String` is a parsable `BigDecimal`. */
  final case class ValidBigDecimal()

  /** Predicate that checks if a `String` is well-formed XML. */
  final case class Xml()

  /** Predicate that checks if a `String` is a valid XPath expression. */
  final case class XPath()
}

private[refined] trait StringValidate {
  implicit def endsWithValidate[S <: String](
      implicit ws: Witness.Aux[S]): Validate.Plain[String, EndsWith[S]] =
    Validate.fromPredicate(_.endsWith(ws.value),
                           t => s""""$t".endsWith("${ws.value}")""",
                           EndsWith(ws.value))

  implicit def ipv4Validate[S <: String]: Validate.Plain[String, IPv4] =
    Validate.fromPredicate(IPv4.predicate, t => s"${t} is a valid IPv4", IPv4())

  implicit def ipv6Validate[S <: String]: Validate.Plain[String, IPv6] =
    Validate.fromPredicate(IPv6.predicate, t => s"${t} is a valid IPv6", IPv6())

  implicit def matchesRegexValidate[S <: String](
      implicit ws: Witness.Aux[S]): Validate.Plain[String, MatchesRegex[S]] =
    Validate.fromPredicate(_.matches(ws.value),
                           t => s""""$t".matches("${ws.value}")""",
                           MatchesRegex(ws.value))

  implicit def regexValidate: Validate.Plain[String, Regex] =
    Validate.fromPartial(new scala.util.matching.Regex(_), "Regex", Regex())

  implicit def startsWithValidate[S <: String](
      implicit ws: Witness.Aux[S]): Validate.Plain[String, StartsWith[S]] =
    Validate.fromPredicate(_.startsWith(ws.value),
                           t => s""""$t".startsWith("${ws.value}")""",
                           StartsWith(ws.value))

  implicit def uriValidate: Validate.Plain[String, Uri] =
    Validate.fromPartial(new java.net.URI(_), "Uri", Uri())

  implicit def urlValidate: Validate.Plain[String, Url] =
    Validate.fromPartial(new java.net.URL(_), "Url", Url())

  implicit def uuidValidate: Validate.Plain[String, Uuid] =
    Validate.fromPartial(s => require(java.util.UUID.fromString(s).toString == s.toLowerCase),
                         "Uuid",
                         Uuid())

  implicit def validIntValidate: Validate.Plain[String, ValidInt] =
    Validate.fromPartial(_.toInt, "ValidInt", ValidInt())

  implicit def validLongValidate: Validate.Plain[String, ValidLong] =
    Validate.fromPartial(_.toLong, "ValidLong", ValidLong())

  implicit def validDoubleValidate: Validate.Plain[String, ValidDouble] =
    Validate.fromPartial(_.toDouble, "ValidDouble", ValidDouble())

  implicit def validBigIntValidate: Validate.Plain[String, ValidBigInt] =
    Validate.fromPartial(BigInt(_), "ValidBigInt", ValidBigInt())

  implicit def validBigDecimalValidate: Validate.Plain[String, ValidBigDecimal] =
    Validate.fromPartial(BigDecimal(_), "ValidBigDecimal", ValidBigDecimal())

  implicit def xmlValidate: Validate.Plain[String, Xml] =
    Validate.fromPartial(scala.xml.XML.loadString, "Xml", Xml())

  implicit def xpathValidate: Validate.Plain[String, XPath] =
    Validate
      .fromPartial(javax.xml.xpath.XPathFactory.newInstance().newXPath().compile, "XPath", XPath())
}

private[refined] trait StringInference {

  implicit def endsWithInference[A <: String, B <: String](
      implicit wa: Witness.Aux[A],
      wb: Witness.Aux[B]
  ): EndsWith[A] ==> EndsWith[B] =
    Inference(wa.value.endsWith(wb.value), s"endsWithInference(${wa.value}, ${wb.value})")

  implicit def startsWithInference[A <: String, B <: String](
      implicit wa: Witness.Aux[A],
      wb: Witness.Aux[B]
  ): StartsWith[A] ==> StartsWith[B] =
    Inference(wa.value.startsWith(wb.value), s"startsWithInference(${wa.value}, ${wb.value})")
}
