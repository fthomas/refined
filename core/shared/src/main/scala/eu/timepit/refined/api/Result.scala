package eu.timepit.refined
package api

sealed abstract class Result[A] extends Product with Serializable {

  def detail: A

  def as[B](b: B): Result[B] =
    map(_ => b)

  def fold[B](ifPassed: A => B, ifFailed: A => B): B =
    this match {
      case Passed(d) => ifPassed(d)
      case Failed(d) => ifFailed(d)
    }

  def isPassed: Boolean =
    morph(true, false)

  def isFailed: Boolean =
    morph(false, true)

  def map[B](f: A => B): Result[B] =
    fold(a => Passed(f(a)), a => Failed(f(a)))

  def morph[B](ifPassed: B, ifFailed: B): B =
    fold(_ => ifPassed, _ => ifFailed)
}

final case class Passed[A](detail: A) extends Result[A]
final case class Failed[A](detail: A) extends Result[A]

object Result {

  def fromBoolean[A](b: Boolean, detail: A): Result[A] =
    if (b) Passed(detail) else Failed(detail)
}
