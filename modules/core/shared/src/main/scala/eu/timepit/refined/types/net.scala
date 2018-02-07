package eu.timepit.refined.types

import eu.timepit.refined.W
import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.boolean.{And, Or}
import eu.timepit.refined.numeric.Interval
import eu.timepit.refined.string.{IPv4, MatchesRegex, StartsWith}

/** Module for refined types that are related to the Internet protocol suite. */
object net {

  /** An `Int` in the range from 0 to 65535 representing a port number. */
  type PortNumber = Int Refined Interval.Closed[W.`0`.T, W.`65535`.T]

  object PortNumber extends RefinedTypeOps[PortNumber, Int]

  /** An `Int` in the range from 0 to 1023 representing a port number. */
  type SystemPortNumber = Int Refined Interval.Closed[W.`0`.T, W.`1023`.T]

  object SystemPortNumber extends RefinedTypeOps[SystemPortNumber, Int]

  /** An `Int` in the range from 1024 to 49151 representing a port number. */
  type UserPortNumber = Int Refined Interval.Closed[W.`1024`.T, W.`49151`.T]

  object UserPortNumber extends RefinedTypeOps[UserPortNumber, Int]

  /** An `Int` in the range from 49152 to 65535 representing a port number. */
  type DynamicPortNumber = Int Refined Interval.Closed[W.`49152`.T, W.`65535`.T]

  object DynamicPortNumber extends RefinedTypeOps[DynamicPortNumber, Int]

  /** An `Int` in the range from 1024 to 65535 representing a port number. */
  type NonSystemPortNumber = Int Refined Interval.Closed[W.`1024`.T, W.`65535`.T]

  object NonSystemPortNumber extends RefinedTypeOps[NonSystemPortNumber, Int]

  import PrivateNetworks._

  /** A `String` representing a valid IPv4 in the private network 10.0.0.0/8 (RFC1918) */
  type Rfc1918ClassAPrivate = String Refined Rfc1918ClassAPrivateSpec

  /** A `String` representing a valid IPv4 in the private network 172.15.0.0/12 (RFC1918) */
  type Rfc1918ClassBPrivate = String Refined Rfc1918ClassBPrivateSpec

  /** A `String` representing a valid IPv4 in the private network 192.168.0.0/16 (RFC1918) */
  type Rfc1918ClassCPrivate = String Refined Rfc1918ClassCPrivateSpec

  /** A `String` representing a valid IPv4 in a private network according to RFC1918 */
  type Rfc1918Private = String Refined Rfc1918PrivateSpec

  /** A `String` representing a valid IPv4 in the private network 192.0.2.0/24 (RFC5737) */
  type Rfc5737Testnet1 = String Refined Rfc5737Testnet1Spec

  /** A `String` representing a valid IPv4 in the private network 198.51.100.0/24 (RFC5737) */
  type Rfc5737Testnet2 = String Refined Rfc5737Testnet2Spec

  /** A `String` representing a valid IPv4 in the private network 203.0.113.0/24 (RFC5737) */
  type Rfc5737Testnet3 = String Refined Rfc5737Testnet3Spec

  /** A `String` representing a valid IPv4 in a private network according to RFC5737 */
  type Rfc5737Testnet = String Refined Rfc5737TestnetSpec

  /** A `String` representing a valid IPv4 in the local link network 169.254.0.0/16 (RFC3927) */
  type Rfc3927LocalLink = String Refined Rfc3927LocalLinkSpec

  /** A `String` representing a valid IPv4 in the benchmarking network 198.18.0.0/15 (RFC2544) */
  type Rfc2544Benchmark = String Refined Rfc2544BenchmarkSpec

  /** A `String` representing a valid IPv4 in a private network according to RFC1918, RFC5737, RFC3927 or RFC2544  */
  type PrivateNetwork =
    String Refined (Rfc1918PrivateSpec Or Rfc5737TestnetSpec Or Rfc3927LocalLinkSpec Or Rfc2544BenchmarkSpec)

  object PrivateNetworks {

    type Rfc1918ClassAPrivateSpec =
      IPv4 And StartsWith[W.`"10."`.T]

    type Rfc1918ClassBPrivateSpec =
      IPv4 And MatchesRegex[W.`"^172\\\\.(15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31).+"`.T]

    type Rfc1918ClassCPrivateSpec =
      IPv4 And StartsWith[W.`"192.168."`.T]

    type Rfc1918PrivateSpec =
      Rfc1918ClassAPrivateSpec Or Rfc1918ClassBPrivateSpec Or Rfc1918ClassCPrivateSpec

    type Rfc5737Testnet1Spec =
      IPv4 And StartsWith[W.`"192.0.2."`.T]

    type Rfc5737Testnet2Spec =
      IPv4 And StartsWith[W.`"198.51.100."`.T]

    type Rfc5737Testnet3Spec =
      IPv4 And StartsWith[W.`"203.0.113."`.T]

    type Rfc5737TestnetSpec =
      Rfc5737Testnet1Spec Or Rfc5737Testnet2Spec Or Rfc5737Testnet3Spec

    type Rfc3927LocalLinkSpec =
      IPv4 And StartsWith[W.`"169.254."`.T]

    type Rfc2544BenchmarkSpec =
      IPv4 And Or[StartsWith[W.`"198.18."`.T], StartsWith[W.`"198.19."`.T]]
  }
}

trait NetTypes {
  final type PortNumber = net.PortNumber
  final val PortNumber = net.PortNumber

  final type SystemPortNumber = net.SystemPortNumber
  final val SystemPortNumber = net.SystemPortNumber

  final type UserPortNumber = net.UserPortNumber
  final val UserPortNumber = net.UserPortNumber

  final type DynamicPortNumber = net.DynamicPortNumber
  final val DynamicPortNumber = net.DynamicPortNumber

  final type NonSystemPortNumber = net.NonSystemPortNumber
  final val NonSystemPortNumber = net.NonSystemPortNumber

  final type Rfc1918ClassAPrivate = net.Rfc1918ClassAPrivate

  final type Rfc1918ClassBPrivate = net.Rfc1918ClassBPrivate

  final type Rfc1918ClassCPrivate = net.Rfc1918ClassCPrivate

  final type Rfc1918Private = net.Rfc1918Private

  final type Rfc5737Testnet1 = net.Rfc5737Testnet1

  final type Rfc5737Testnet2 = net.Rfc5737Testnet2

  final type Rfc5737Testnet3 = net.Rfc5737Testnet3

  final type Rfc5737Testnet = net.Rfc5737Testnet

  final type Rfc3927LocalLink = net.Rfc3927LocalLink

  final type Rfc2544Benchmark = net.Rfc2544Benchmark

  final type PrivateNetwork = net.PrivateNetwork
}
