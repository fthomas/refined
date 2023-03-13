package eu.timepit.refined.pureconfig

import scala.quoted.{Expr, Quotes, Type, quotes}

final class DescribeType[T](val repr: String)

object DescribeType:

  private def macroImpl[T: Type](using Quotes): Expr[DescribeType[T]] = {
    import quotes.reflect.*
    '{ new DescribeType[T](${ Expr(TypeRepr.of[T].show) }) }
  }

  inline given describeType[T]: DescribeType[T] = ${ macroImpl[T] }

private[pureconfig] trait TypeDescribeVersionSpecific {

  protected def getDescription[A](d: DescribeType[A]): String = d.repr
}
