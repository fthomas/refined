package eu.timepit.refined
package examples

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.boolean.True
import eu.timepit.refined.string.MatchesRegex

// inspired by http://meta.plasm.us/posts/2015/11/08/type-classes-and-generic-derivation/

case class Person(name: String, age: Int)

trait Parser[A] {
  type P
  def parse(s: String Refined P): A
}

object Parser {
  type Aux[A, P0] = Parser[A] { type P = P0 }

  def apply[A](implicit parser: Parser[A]): Parser.Aux[A, parser.P] = parser

  implicit val stringParser: Parser.Aux[String, True] = new Parser[String] {
    type P = True
    def parse(s: String Refined P): String = s
  }

  implicit val intParser: Parser.Aux[Int, MatchesRegex[W.`"""\\-?\\d+"""`.T]] =
    new Parser[Int] {
      type P = MatchesRegex[W.`"""\\-?\\d+"""`.T]
      def parse(s: String Refined P): Int = s.get.toInt
    }

  implicit val personParser: Parser.Aux[
      Person, MatchesRegex[W.`"""[^,]*,\\d+"""`.T]] = new Parser[Person] {
    type P = MatchesRegex[W.`"""[^,]*,\\d+"""`.T]
    def parse(s: String Refined P): Person = {
      val parts = s.split(",")
      Person(parts(0), parts(1).toInt)
    }
  }
}

object Derivation extends App {

  val x: Int = Parser[Int].parse("123")
  assert(x == 123)

  val bob: Person = Parser[Person].parse("Bob,32")
  assert(bob == Person("Bob", 32))

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
