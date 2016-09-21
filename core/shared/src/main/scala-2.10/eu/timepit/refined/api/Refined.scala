package eu.timepit.refined
package api

/**
 * Wraps a value of type `T` that satisfies the predicate `P`. Instances of
 * this class can be created with `[[refineV]]` and `[[refineMV]]` which
 * verify that the wrapped value satisfies `P`.
 */
final class Refined[T, P] private (val get: T) extends Serializable {

  override def hashCode: Int =
    get.hashCode

  override def equals(that: Any): Boolean =
    that match {
      case that: Refined[_, _] => this.get == that.get
      case _ => false
    }

  override def toString: String =
    get.toString
}

object Refined {

  /**
   * Wraps `t` in `[[Refined]][T, P]` ''without'' verifying that it satisfies
   * the predicate `P`. This method is for internal use only.
   */
  def unsafeApply[T, P](t: T): Refined[T, P] =
    new Refined(t)

  def unapply[T, P](r: Refined[T, P]): Some[T] =
    Some(r.get)
}

/*
Refined is not value class for Scala 2.10 because that would trigger
compiler errors like this:

[error] refined/core/shared/src/main/scala/eu/timepit/refined/api/RefType.scala:152:
  bridge generated for member method unsafeWrap: [T, P](t: T)eu.timepit.refined.api.Refined[T,P]
  in anonymous class $anon
[error] which overrides method unsafeWrap: [T, P](t: T)F[T,P] in trait RefType
[error] clashes with definition of the member itself;
[error] both have erased type (t: Object)Object
[error]       override def unsafeWrap[T, P](t: T): Refined[T, P] =
[error]                    ^

See https://issues.scala-lang.org/browse/SI-6260 for more information.
 */
