package eu.timepit.refined
package smt

import scala.sys.process._

object smtlib {

  case class Sort[T](asString: String)

  object Sort {
    def apply[T](implicit sort: Sort[T]): Sort[T] = sort

    def builtinBool[T]: Sort[T] =
      Sort("Bool")

    def builtinInt[T]: Sort[T] =
      Sort("Int")

    def builtinReal[T]: Sort[T] =
      Sort("Real")

    implicit val byteSort: Sort[Byte] =
      builtinInt

    implicit val booleanSort: Sort[Boolean] =
      builtinBool

    implicit val doubleSort: Sort[Double] =
      builtinReal

    implicit val floatSort: Sort[Float] =
      builtinReal

    implicit val intSort: Sort[Int] =
      builtinInt

    implicit val longSort: Sort[Long] =
      builtinInt

    implicit val shortSort: Sort[Short] =
      builtinInt
  }

  def assert(f: String): String =
    s"(assert $f)"

  def assertNegation(f: String): String =
    assert(s"(not $f)")

  val assertInference: String =
    assertNegation("inference")

  val checkSat: String =
    "(check-sat)"

  def declareConst[T](name: String)(implicit sort: Sort[T]): String =
    s"(declare-const $name ${sort.asString})"

  def defineValue[T](name: String, value: T)(implicit sort: Sort[T]): String =
    s"(define-fun $name () ${sort.asString} $value)"

  def implies(a: String, b: String): String =
    s"(=> $a $b)"

  def defineInference(a: String, b: String): String =
    s"(define-fun inference () ${Sort.builtinBool.asString} ${implies(a, b)})"

  def unsafeInvokeZ3(script: String): String =
    (s"echo $script" #| "z3 -in").!!.trim
}
