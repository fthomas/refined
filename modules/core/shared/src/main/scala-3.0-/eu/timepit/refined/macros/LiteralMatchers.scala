package eu.timepit.refined.macros

import scala.reflect.macros.blackbox

trait LiteralMatchers {
  val c: blackbox.Context
  import c.universe._

  private[macros] object BigIntMatcher {
    def unapply(expr: c.Tree): Option[BigInt] =
      expr match {
        case q"scala.`package`.BigInt.apply(${lit: Literal})" =>
          lit.value.value match {
            case i: Int    => Some(BigInt(i))
            case l: Long   => Some(BigInt(l))
            case s: String => scala.util.Try(BigInt(s)).toOption
            case _         => None
          }
        case _ => None
      }
  }

  private[macros] object BigDecimalMatcher {
    def unapply(expr: c.Tree): Option[BigDecimal] = {
      val constant = expr match {
        case q"scala.`package`.BigDecimal.apply(${lit: Literal})"   => Some(lit.value.value)
        case q"scala.`package`.BigDecimal.exact(${lit: Literal})"   => Some(lit.value.value)
        case q"scala.`package`.BigDecimal.valueOf(${lit: Literal})" => Some(lit.value.value)
        case _                                                      => None
      }

      constant.flatMap {
        case i: Int    => Some(BigDecimal(i))
        case l: Long   => Some(BigDecimal(l))
        case d: Double => Some(BigDecimal(d))
        case s: String => scala.util.Try(BigDecimal(s)).toOption
        case _         => None
      }
    }
  }

}
