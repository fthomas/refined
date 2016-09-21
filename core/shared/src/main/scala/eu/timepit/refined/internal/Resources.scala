package eu.timepit.refined
package internal

import eu.timepit.refined.api.Result
import scala.util.{Failure, Success, Try}

object Resources {
  val passed = "passed"
  val failed = "failed"

  val Predicate = "Predicate"
  val predicate = Predicate.toLowerCase
  val predicates = s"${predicate}s"

  val Both = "Both"
  val Left = "Left"
  val Right = "Right"

  def predicateResult(r: Result[_]): String =
    s"$Predicate ${toLowerCase(r)}"

  def predicateResultDetail(r: Result[_], detail: String): String =
    s"${predicateResult(r)}: $detail"

  def predicateResultDetailDot(r: Result[_], detail: String): String =
    s"${predicateResultDetail(r, detail)}."

  def predicateTakingResultDetail(taking: String, r: Result[_], detail: String): String =
    s"$Predicate taking $taking ${toLowerCase(r)}: $detail"

  def showExprEmptyCollection: String =
    "<no element>"

  def showResultEmptyCollection: String =
    s"$Predicate $failed: empty collection."

  def namePredicateResult(name: String, r: Result[_]): String =
    s"$name $predicate ${toLowerCase(r)}"

  def namePredicateResultMessage(name: String, r: Result[_], maybeThrowable: Try[_]): String = {
    val suffix = maybeThrowable match {
      case Success(_) => "."
      case Failure(e) => s": ${e.getMessage}"
    }
    namePredicateResult(name, r) + suffix
  }

  def isValidName[T](name: String, t: T): String =
    s"""isValid$name("$t")"""

  def toLowerCase(r: Result[_]): String =
    r.morph(passed, failed)

  // Not

  def showResultNotInnerPassed(expr: String): String =
    s"$Predicate $expr did not fail."

  def showResultNotInnerFailed(expr: String): String =
    s"$Predicate $expr did not pass."

  // And

  def showResultAndBothPassed(expr: String): String =
    s"$Both $predicates of $expr $passed."

  def showResultAndRightFailed(expr: String, right: String): String =
    s"$Right $predicate of $expr $failed: $right"

  def showResultAndLeftFailed(expr: String, left: String): String =
    s"$Left $predicate of $expr $failed: $left"

  def showResultAndBothFailed(expr: String, left: String, right: String): String =
    s"$Both $predicates of $expr $failed. $Left: $left $Right: $right"

  // Or

  def showResultOrBothPassed(expr: String): String =
    s"$Both $predicates of $expr $passed."

  def showResultOrRightPassed(expr: String): String =
    s"$Right $predicate of $expr $passed."

  def showResultOrLeftPassed(expr: String): String =
    s"$Left $predicate of $expr $passed."

  def showResultOrBothFailed(expr: String, left: String, right: String): String =
    s"$Both $predicates of $expr $failed. $Left: $left $Right: $right"

  //

  val refineNonCompileTimeConstant =
    "compile-time refinement only works with literals"

  def invalidInference(from: String, to: String): String =
    s"""type mismatch (invalid inference):
       | $from does not imply
       | $to
     """.stripMargin.trim
}
