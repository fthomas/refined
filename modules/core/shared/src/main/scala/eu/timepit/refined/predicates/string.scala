package eu.timepit.refined.predicates

import eu.timepit.refined

object string extends StringPredicates with StringPredicatesBinCompat1

trait StringPredicates {
  final type EndsWith[S] = refined.string.EndsWith[S]
  final val EndsWith = refined.string.EndsWith

  final type IPv4 = refined.string.IPv4
  final val IPv4 = refined.string.IPv4

  final type IPv6 = refined.string.IPv6
  final val IPv6 = refined.string.IPv6

  final type MatchesRegex[S] = refined.string.MatchesRegex[S]
  final val MatchesRegex = refined.string.MatchesRegex

  final type Regex = refined.string.Regex
  final val Regex = refined.string.Regex

  final type StartsWith[S] = refined.string.StartsWith[S]
  final val StartsWith = refined.string.StartsWith

  final type Uri = refined.string.Uri
  final val Uri = refined.string.Uri

  final type Url = refined.string.Url
  final val Url = refined.string.Url

  final type Uuid = refined.string.Uuid
  final val Uuid = refined.string.Uuid

  final type ValidInt = refined.string.ValidInt
  final val ValidInt = refined.string.ValidInt

  final type ValidLong = refined.string.ValidLong
  final val ValidLong = refined.string.ValidLong

  final type ValidDouble = refined.string.ValidDouble
  final val ValidDouble = refined.string.ValidDouble

  final type ValidBigInt = refined.string.ValidBigInt
  final val ValidBigInt = refined.string.ValidBigInt

  final type ValidBigDecimal = refined.string.ValidBigDecimal
  final val ValidBigDecimal = refined.string.ValidBigDecimal

  final type Xml = refined.string.Xml
  final val Xml = refined.string.Xml

  final type XPath = refined.string.XPath
  final val XPath = refined.string.XPath
}

trait StringPredicatesBinCompat1 {
  final type Trimmed = refined.string.Trimmed
  final val Trimmed = refined.string.Trimmed

  final type ValidByte = refined.string.ValidByte
  final val ValidByte = refined.string.ValidByte

  final type ValidShort = refined.string.ValidShort
  final val ValidShort = refined.string.ValidShort

  final type ValidFloat = refined.string.ValidFloat
  final val ValidFloat = refined.string.ValidFloat

  final type HexStringSpec = refined.string.HexStringSpec
}
