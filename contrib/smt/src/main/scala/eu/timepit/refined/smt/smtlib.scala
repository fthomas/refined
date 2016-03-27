package eu.timepit.refined
package smt

import scala.sys.process._

object smtlib {

  case class Show[T](asString: T => String)

  object Show extends LowPriorityShow {
    def apply[T](implicit show: Show[T]): Show[T] = show

    implicit def listShow[T](implicit show: Show[T], sort: Sort[List[T]]): Show[List[T]] =
      Show(_.foldRight(s"(as nil ${sort.asString})")((t, acc) => s"(insert ${show.asString(t)} $acc)"))
  }

  trait LowPriorityShow {
    implicit def defaultShow[T]: Show[T] =
      Show(_.toString)
  }

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

    implicit def listSort[T](implicit sort: Sort[T]): Sort[List[T]] =
      Sort(s"(List ${sort.asString})")
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

  def defineValue[T](name: String, value: T)(implicit sort: Sort[T], show: Show[T]): String =
    s"(define-fun $name () ${sort.asString} ${show.asString(value)})"

  def implies(a: String, b: String): String =
    s"(=> $a $b)"

  def defineInference(a: String, b: String): String =
    s"(define-fun inference () ${Sort.builtinBool.asString} ${implies(a, b)})"

  val sat: String =
    "sat"

  val unsat: String =
    "unsat"

  def unsafeInvokeZ3(script: String): String = {
    def appendTo(sb: StringBuilder)(line: String): Unit = {
      sb.append(line)
      sb.append(System.lineSeparator())
      ()
    }

    val outBuf = new StringBuilder
    val errBuf = new StringBuilder
    val logger = ProcessLogger(appendTo(outBuf), appendTo(errBuf))

    val timeoutInSeconds = 10
    val timeoutInMillis = timeoutInSeconds * 1000

    val process = Process(s"echo $script")
      .#|(s"z3 -T:$timeoutInSeconds -t:$timeoutInMillis -in")
    val status = process.run(logger).exitValue()
    val out = outBuf.result().trim

    if (status != 0) {
      val err = errBuf.result().trim
      val msg = s"""
        |"$script" failed with status $status
        |  stdout: $out
        |  stderr: $err
      """.stripMargin.trim
      throw new java.io.IOException(msg)
    }
    out
  }
}
