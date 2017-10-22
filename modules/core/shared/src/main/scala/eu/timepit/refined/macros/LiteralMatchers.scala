package eu.timepit.refined.macros

import macrocompat.bundle

@bundle
trait LiteralMatchers {
  this: MacroUtils =>

  import c.universe._

  private[macros] object BigIntMatcher {
    private val matcher = Unliftable[Constant] {
      case q"scala.`package`.BigInt.apply(${lit: Literal})" => lit.value
    }

    def unapply(expr: c.Tree): Option[BigInt] = matcher.unapply(expr).flatMap { lit =>
      lit.value match {
        case i: Int    => Some(BigInt(i))
        case l: Long   => Some(BigInt(l))
        case s: String => scala.util.Try(BigInt(s)).toOption
        case _         => None
      }
    }
  }

  private[macros] object BigDecimalMatcher {
    private val matcher = Unliftable[Constant] {
      case q"scala.`package`.BigDecimal.apply(${lit: Literal})"   => lit.value
      case q"scala.`package`.BigDecimal.exact(${lit: Literal})"   => lit.value
      case q"scala.`package`.BigDecimal.valueOf(${lit: Literal})" => lit.value
    }

    def unapply(expr: c.Tree): Option[BigDecimal] = matcher.unapply(expr).flatMap { lit =>
      lit.value match {
        case i: Int    => Some(BigDecimal(i))
        case l: Long   => Some(BigDecimal(l))
        case d: Double => Some(BigDecimal(d))
        case s: String => scala.util.Try(BigDecimal(s)).toOption
        case _         => None
      }
    }
  }

}
