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
object string extends StringInference {

  /** Predicate that checks if a `String` ends with the suffix `S`. */
  final case class EndsWith[S](s: S)

  /** Predicate that checks if a `String` is a valid IPv4 */
  final case class IPv4()

  /** Predicate that checks if a `String` is a valid IPv6 */
  final case class IPv6()

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

  /** Predicate that checks if a `String` is a parsable `Byte`. */
  final case class ValidByte()

  /** Predicate that checks if a `String` is a parsable `Short`. */
  final case class ValidShort()

  /** Predicate that checks if a `String` is a parsable `Int`. */
  final case class ValidInt()

  /** Predicate that checks if a `String` is a parsable `Long`. */
  final case class ValidLong()

  /** Predicate that checks if a `String` is a parsable `Float`. */
  final case class ValidFloat()

  /** Predicate that checks if a `String` is a parsable `Double`. */
  final case class ValidDouble()

  /** Predicate that checks if a `String` is a parsable `BigInt`. */
  final case class ValidBigInt()

  /** Predicate that checks if a `String` is a parsable `BigDecimal`. */
  final case class ValidBigDecimal()

  /** Predicate that checks if a `String` is a valid XPath expression. */
  final case class XPath()

  /** Predicate that checks if a `String` has no leading or trailing whitespace. */
  final case class Trimmed()

  /** Predicate that checks if a `String` represents a hexadecimal number. */
  type HexStringSpec = MatchesRegex[W.`"""^(([0-9a-f]+)|([0-9A-F]+))$"""`.T]

  object EndsWith {
    implicit def endsWithValidate[S <: String](
        implicit ws: Witness.Aux[S]
    ): Validate.Plain[String, EndsWith[S]] =
      Validate.fromPredicate(
        _.endsWith(ws.value),
        t => s""""$t".endsWith("${ws.value}")""",
        EndsWith(ws.value)
      )
  }

  object IPv4 {
    implicit def ipv4Validate: Validate.Plain[String, IPv4] =
      Validate.fromPredicate(predicate, t => s"$t is a valid IPv4", IPv4())

    private val regex = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$".r.pattern
    private val maxOctet = 255
    private val predicate: String => Boolean = s => {
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

  object IPv6 {
    implicit def ipv6Validate: Validate.Plain[String, IPv6] =
      Validate.fromPredicate(predicate, t => s"$t is a valid IPv6", IPv6())

    private val ipv4Chars = "(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])"
    private val ipv4 = s"($ipv4Chars\\.){3,3}$ipv4Chars"
    private val ipv6Chars = "[0-9a-fA-F]{1,4}"

    private val ipv6Full = s"($ipv6Chars:){7,7}$ipv6Chars" // 1:2:3:4:5:6:7:8

    private val ipv6Compact = List(
      s"($ipv6Chars:){1,7}:", // 1:: .. 1:2:3:4:5:6:7::
      s"($ipv6Chars:){1,6}:$ipv6Chars", // 1::8 .. 1:2:3:4:5:6::8
      s"($ipv6Chars:){1,5}(:$ipv6Chars){1,2}", // 1::7:8 .. 1:2:3:4:5::8
      s"($ipv6Chars:){1,4}(:$ipv6Chars){1,3}", // 1::6:7:8 .. 1:2:3:4::8
      s"($ipv6Chars:){1,3}(:$ipv6Chars){1,4}", // 1::5:6:7:8 .. 1:2:3::8
      s"($ipv6Chars:){1,2}(:$ipv6Chars){1,5}", // 1::4:5:6:7:8 .. 1:2::8
      s"($ipv6Chars:)(:$ipv6Chars){1,6}", // 1::3:4:5:6:7:8 .. 1::8
      s":((:$ipv6Chars){1,7}|:)" // ::2:3:4:5:6:7:8 .. ::
    )

    private val interface = "[0-9a-zA-Z]{1,}"
    private val ipv6LinkLocal = s"fe80:(:$ipv6Chars){0,4}%$interface"

    private val mappedIpv6 = s"::(ffff(:0{1,4}){0,1}:){0,1}$ipv4"

    private val embeddedIpv4 = s"($ipv6Chars:){1,4}:$ipv4"

    private val formats = List(
      ipv6Full,
      ipv6LinkLocal,
      mappedIpv6,
      embeddedIpv4
    ) ++ ipv6Compact

    private val regex = formats.map(regex => s"(^$regex$$)").mkString("|").r.pattern
    private val predicate: String => Boolean = s => regex.matcher(s).matches
  }

  object MatchesRegex {
    implicit def matchesRegexValidate[S <: String](
        implicit ws: Witness.Aux[S]
    ): Validate.Plain[String, MatchesRegex[S]] =
      Validate.fromPredicate(
        _.matches(ws.value),
        t => s""""$t".matches("${ws.value}")""",
        MatchesRegex(ws.value)
      )
  }

  object Regex {
    implicit def regexValidate: Validate.Plain[String, Regex] =
      Validate.fromPartial(new scala.util.matching.Regex(_), "Regex", Regex())
  }

  object StartsWith {
    implicit def startsWithValidate[S <: String](
        implicit ws: Witness.Aux[S]
    ): Validate.Plain[String, StartsWith[S]] =
      Validate.fromPredicate(
        _.startsWith(ws.value),
        t => s""""$t".startsWith("${ws.value}")""",
        StartsWith(ws.value)
      )
  }

  object Uri {
    implicit def uriValidate: Validate.Plain[String, Uri] =
      Validate.fromPartial(new java.net.URI(_), "Uri", Uri())
  }

  object Url {
    implicit def urlValidate: Validate.Plain[String, Url] =
      Validate.fromPartial(new java.net.URL(_), "Url", Url())
  }

  object Uuid {
    implicit def uuidValidate: Validate.Plain[String, Uuid] =
      Validate.fromPartial(
        s => require(java.util.UUID.fromString(s).toString == s.toLowerCase),
        "Uuid",
        Uuid()
      )
  }

  object ValidByte {
    implicit def validByteValidate: Validate.Plain[String, ValidByte] =
      Validate.fromPartial(_.toByte, "ValidByte", ValidByte())
  }

  object ValidShort {
    implicit def validShortValidate: Validate.Plain[String, ValidShort] =
      Validate.fromPartial(_.toShort, "ValidShort", ValidShort())
  }

  object ValidInt {
    implicit def validIntValidate: Validate.Plain[String, ValidInt] =
      Validate.fromPartial(_.toInt, "ValidInt", ValidInt())
  }

  object ValidLong {
    implicit def validLongValidate: Validate.Plain[String, ValidLong] =
      Validate.fromPartial(_.toLong, "ValidLong", ValidLong())
  }

  object ValidFloat {
    implicit def validFloatValidate: Validate.Plain[String, ValidFloat] =
      Validate.fromPartial(_.toFloat, "ValidFloat", ValidFloat())
  }

  object ValidDouble {
    implicit def validDoubleValidate: Validate.Plain[String, ValidDouble] =
      Validate.fromPartial(_.toDouble, "ValidDouble", ValidDouble())
  }

  object ValidBigInt {
    implicit def validBigIntValidate: Validate.Plain[String, ValidBigInt] =
      Validate.fromPartial(BigInt(_), "ValidBigInt", ValidBigInt())
  }

  object ValidBigDecimal {
    implicit def validBigDecimalValidate: Validate.Plain[String, ValidBigDecimal] =
      Validate.fromPartial(BigDecimal(_), "ValidBigDecimal", ValidBigDecimal())
  }

  object XPath {
    implicit def xpathValidate: Validate.Plain[String, XPath] =
      Validate.fromPartial(
        javax.xml.xpath.XPathFactory.newInstance().newXPath().compile,
        "XPath",
        XPath()
      )
  }

  object Trimmed {
    implicit def trimmedValidate: Validate.Plain[String, Trimmed] =
      Validate.fromPredicate(s => s.trim == s, t => s"$t is trimmed", Trimmed())
  }
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
