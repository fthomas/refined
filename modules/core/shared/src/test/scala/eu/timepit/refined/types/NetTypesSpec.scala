package eu.timepit.refined.types

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.types.net.PrivateNetworks._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class NetTypesSpec extends Properties("NetTypes") {

  property("Rfc1918ClassAPrivateSpec.before") = secure {
    showResult[Rfc1918ClassAPrivateSpec]("9.255.255.255") ?=
      """Right predicate of (9.255.255.255 is a valid IPv4 && "9.255.255.255".startsWith("10.")) failed: Predicate failed: "9.255.255.255".startsWith("10.")."""
  }

  property("Rfc1918ClassAPrivateSpec.isValid.first") = secure {
    isValid[Rfc1918ClassAPrivateSpec]("10.0.0.0")
  }

  property("Rfc1918ClassAPrivateSpec.isValid.inside") = secure {
    isValid[Rfc1918ClassAPrivateSpec]("10.200.18.255")
  }

  property("Rfc1918ClassAPrivateSpec.isValid.last") = secure {
    isValid[Rfc1918ClassAPrivateSpec]("10.255.255.255")
  }

  property("Rfc1918ClassAPrivateSpec.after") = secure {
    showResult[Rfc1918ClassAPrivateSpec]("11.0.0.0") ?=
      """Right predicate of (11.0.0.0 is a valid IPv4 && "11.0.0.0".startsWith("10.")) failed: Predicate failed: "11.0.0.0".startsWith("10.")."""
  }

  property("Rfc1918ClassBPrivateSpec.before") = secure {
    showResult[Rfc1918ClassBPrivateSpec]("172.14.255.255") ?=
      """Right predicate of (172.14.255.255 is a valid IPv4 && "172.14.255.255".matches("^172\.(15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)\..+")) failed: Predicate failed: "172.14.255.255".matches("^172\.(15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)\..+")."""
  }

  property("Rfc1918ClassBPrivateSpec.isValid.first") = secure {
    isValid[Rfc1918ClassBPrivateSpec]("172.15.0.0")
  }

  property("Rfc1918ClassBPrivateSpec.isValid.inside") = secure {
    isValid[Rfc1918ClassBPrivateSpec]("172.23.17.172")
  }

  property("Rfc1918ClassBPrivateSpec.isValid.last") = secure {
    isValid[Rfc1918ClassBPrivateSpec]("172.31.255.255")
  }

  property("Rfc1918ClassBPrivateSpec.after") = secure {
    showResult[Rfc1918ClassBPrivateSpec]("172.32.0.0") ?=
      """Right predicate of (172.32.0.0 is a valid IPv4 && "172.32.0.0".matches("^172\.(15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)\..+")) failed: Predicate failed: "172.32.0.0".matches("^172\.(15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)\..+")."""
  }

  property("Rfc1918ClassBPrivateSpec.issue795") = secure {
    showResult[Rfc1918ClassBPrivateSpec]("172.222.255.255") ?=
      """Right predicate of (172.222.255.255 is a valid IPv4 && "172.222.255.255".matches("^172\.(15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)\..+")) failed: Predicate failed: "172.222.255.255".matches("^172\.(15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)\..+")."""
  }

  property("Rfc1918ClassCPrivateSpec.before") = secure {
    showResult[Rfc1918ClassCPrivateSpec]("192.167.255.255") ?=
      """Right predicate of (192.167.255.255 is a valid IPv4 && "192.167.255.255".startsWith("192.168.")) failed: Predicate failed: "192.167.255.255".startsWith("192.168.")."""
  }

  property("Rfc1918ClassCPrivateSpec.isValid.first") = secure {
    isValid[Rfc1918ClassCPrivateSpec]("192.168.0.0")
  }

  property("Rfc1918ClassCPrivateSpec.isValid.inside") = secure {
    isValid[Rfc1918ClassCPrivateSpec]("192.168.100.100")
  }

  property("Rfc1918ClassCPrivateSpec.isValid.last") = secure {
    isValid[Rfc1918ClassCPrivateSpec]("192.168.255.255")
  }

  property("Rfc1918ClassCPrivateSpec.after") = secure {
    showResult[Rfc1918ClassCPrivateSpec]("192.169.0.0") ?=
      """Right predicate of (192.169.0.0 is a valid IPv4 && "192.169.0.0".startsWith("192.168.")) failed: Predicate failed: "192.169.0.0".startsWith("192.168.")."""
  }

  property("Rfc5737Testnet1Spec.before") = secure {
    showResult[Rfc5737Testnet1Spec]("192.0.1.255") ?=
      """Right predicate of (192.0.1.255 is a valid IPv4 && "192.0.1.255".startsWith("192.0.2.")) failed: Predicate failed: "192.0.1.255".startsWith("192.0.2.")."""
  }

  property("Rfc5737Testnet1Spec.isValid.first") = secure {
    isValid[Rfc5737Testnet1Spec]("192.0.2.0")
  }

  property("Rfc5737Testnet1Spec.isValid.inside") = secure {
    isValid[Rfc5737Testnet1Spec]("192.0.2.150")
  }

  property("Rfc5737Testnet1Spec.isValid.last") = secure {
    isValid[Rfc5737Testnet1Spec]("192.0.2.255")
  }

  property("Rfc5737Testnet1Spec.after") = secure {
    showResult[Rfc5737Testnet1Spec]("192.0.3.0") ?=
      """Right predicate of (192.0.3.0 is a valid IPv4 && "192.0.3.0".startsWith("192.0.2.")) failed: Predicate failed: "192.0.3.0".startsWith("192.0.2.")."""
  }

  property("Rfc5737Testnet2Spec.before") = secure {
    showResult[Rfc5737Testnet2Spec]("198.51.99.255") ?=
      """Right predicate of (198.51.99.255 is a valid IPv4 && "198.51.99.255".startsWith("198.51.100.")) failed: Predicate failed: "198.51.99.255".startsWith("198.51.100.")."""
  }

  property("Rfc5737Testnet2Spec.isValid.first") = secure {
    isValid[Rfc5737Testnet2Spec]("198.51.100.0")
  }

  property("Rfc5737Testnet2Spec.isValid.inside") = secure {
    isValid[Rfc5737Testnet2Spec]("198.51.100.200")
  }

  property("Rfc5737Testnet2Spec.isValid.last") = secure {
    isValid[Rfc5737Testnet2Spec]("198.51.100.255")
  }

  property("Rfc5737Testnet2Spec.after") = secure {
    showResult[Rfc5737Testnet2Spec]("198.51.101.0") ?=
      """Right predicate of (198.51.101.0 is a valid IPv4 && "198.51.101.0".startsWith("198.51.100.")) failed: Predicate failed: "198.51.101.0".startsWith("198.51.100.")."""
  }

  property("Rfc5737Testnet3Spec.before") = secure {
    showResult[Rfc5737Testnet3Spec](
      "203.0.112.255"
    ) ?= """Right predicate of (203.0.112.255 is a valid IPv4 && "203.0.112.255".startsWith("203.0.113.")) failed: Predicate failed: "203.0.112.255".startsWith("203.0.113.")."""
  }

  property("Rfc5737Testnet3Spec.isValid.first") = secure {
    isValid[Rfc5737Testnet3Spec]("203.0.113.0")
  }

  property("Rfc5737Testnet3Spec.isValid.inside") = secure {
    isValid[Rfc5737Testnet3Spec]("203.0.113.123")
  }

  property("Rfc5737Testnet3Spec.isValid.last") = secure {
    isValid[Rfc5737Testnet3Spec]("203.0.113.255")
  }

  property("Rfc5737Testnet3Spec.after") = secure {
    showResult[Rfc5737Testnet3Spec]("203.0.114.0") ?=
      """Right predicate of (203.0.114.0 is a valid IPv4 && "203.0.114.0".startsWith("203.0.113.")) failed: Predicate failed: "203.0.114.0".startsWith("203.0.113.")."""
  }

  property("Rfc3927LocalLinkSpec.before") = secure {
    showResult[Rfc3927LocalLinkSpec]("169.253.255.255") ?=
      """Right predicate of (169.253.255.255 is a valid IPv4 && "169.253.255.255".startsWith("169.254.")) failed: Predicate failed: "169.253.255.255".startsWith("169.254.")."""
  }

  property("Rfc3927LocalLinkSpec.isValid.first") = secure {
    isValid[Rfc3927LocalLinkSpec]("169.254.0.0")
  }

  property("Rfc3927LocalLinkSpec.isValid.inside") = secure {
    isValid[Rfc3927LocalLinkSpec]("169.254.213.65")
  }

  property("Rfc3927LocalLinkSpec.isValid.last") = secure {
    isValid[Rfc3927LocalLinkSpec]("169.254.255.255")
  }

  property("Rfc3927LocalLinkSpec.after") = secure {
    showResult[Rfc3927LocalLinkSpec]("169.255.0.0") ?=
      """Right predicate of (169.255.0.0 is a valid IPv4 && "169.255.0.0".startsWith("169.254.")) failed: Predicate failed: "169.255.0.0".startsWith("169.254.")."""
  }

  property("Rfc2544BenchmarkSpec.before") = secure {
    showResult[Rfc2544BenchmarkSpec]("198.17.255.255") ?=
      """Right predicate of (198.17.255.255 is a valid IPv4 && ("198.17.255.255".startsWith("198.18.") || "198.17.255.255".startsWith("198.19."))) failed: Both predicates of ("198.17.255.255".startsWith("198.18.") || "198.17.255.255".startsWith("198.19.")) failed. Left: Predicate failed: "198.17.255.255".startsWith("198.18."). Right: Predicate failed: "198.17.255.255".startsWith("198.19.")."""
  }

  property("Rfc2544BenchmarkSpec.isValid.first") = secure {
    isValid[Rfc2544BenchmarkSpec]("198.18.0.0")
  }

  property("Rfc2544BenchmarkSpec.isValid.inside") = secure {
    isValid[Rfc2544BenchmarkSpec]("198.19.12.2")
  }

  property("Rfc2544BenchmarkSpec.isValid.last") = secure {
    isValid[Rfc2544BenchmarkSpec]("198.19.255.255")
  }

  property("Rfc2544BenchmarkSpec.after") = secure {
    showResult[Rfc2544BenchmarkSpec]("198.20.0.0") ?=
      """Right predicate of (198.20.0.0 is a valid IPv4 && ("198.20.0.0".startsWith("198.18.") || "198.20.0.0".startsWith("198.19."))) failed: Both predicates of ("198.20.0.0".startsWith("198.18.") || "198.20.0.0".startsWith("198.19.")) failed. Left: Predicate failed: "198.20.0.0".startsWith("198.18."). Right: Predicate failed: "198.20.0.0".startsWith("198.19.")."""
  }
}
