package eu.timepit.refined
package examples

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.string.MatchesRegex

// inspired by http://meta.plasm.us/posts/2015/11/08/type-classes-and-generic-derivation/

trait Parser[A] {
  type P <: String
  final type RString = String Refined MatchesRegex[P]
  def parse(s: RString): A
}

object Parser {
  type Aux[A, P0] = Parser[A] { type P = P0 }

  def apply[A](implicit parser: Parser[A]): Parser.Aux[A, parser.P] = parser

  implicit val stringParser: Parser.Aux[String, W.`"""[^,]*,"""`.T] =
    new Parser[String] {
      type P = W.`"""[^,]*,"""`.T
      def parse(s: RString): String = s.get.init
    }

  implicit val intParser: Parser.Aux[Int, W.`"""\\-?\\d+,"""`.T] =
    new Parser[Int] {
      type P = W.`"""\\-?\\d+,"""`.T
      def parse(s: RString): Int = s.get.init.toInt
    }

  implicit def tupleParser[A, B, AR <: String, BR <: String](
    implicit
    pa: Parser.Aux[A, AR],
    pb: Parser.Aux[B, BR],
    c: Concat[AR, BR]
  ): Parser[(A, B)] =
    new Parser[(A, B)] {
      type P = c.Out
      def parse(s: RString): (A, B) = {
        val splitted = s.get.split(",")
        val (h, t) = (splitted.head + ",", splitted.tail.mkString("", ",", ","))

        (pa.parse(Refined.unsafeApply(h)), pb.parse(Refined.unsafeApply(t)))
      }
    }
}

object Derivation extends App {

  val a = Parser[Int].parse("123,")
  assert(a == 123)

  //val p: Parser.Aux[(Int, Int), W.`"""\\-?\\d+,\\-?\\d+,"""`.T] = Parser.tupleParser(Parser.intParser, Parser.intParser, Concat[W.`"""\\-?\\d+,"""`.T, W.`"""\\-?\\d+,"""`.T])

  val b = Parser[(Int, Int)].parse("12,34,")
  //  val b = p.parse("12,34,")
  assert(b == ((12, 34)))

  // Compilation fails for:

  // Parser[Int].parse("12a")
  /*
  [error] Derivation.scala:48: Predicate failed: "12a".matches("\-?\d+").
  [error]   Parser[Int].parse("12a")
  [error]                     ^
  */

  //Parser[Person].parse("32,Bob")
  /*
  [error] Derivation.scala:59: Predicate failed: "32,Bob".matches("[^,]*,\d+").
  [error]   Parser[Person].parse("32,Bob")
  [error]                        ^
  */
}
