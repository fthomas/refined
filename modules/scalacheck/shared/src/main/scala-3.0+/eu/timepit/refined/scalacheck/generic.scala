package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.generic.Equal
import org.scalacheck.{Arbitrary, Gen}

/** Module that provides `Arbitrary` instances for generic predicates. */
object generic extends GenericInstances

trait GenericInstances {

  implicit def equalArbitrary[F[_, _]: RefType, T, U <: T](implicit
      wu: ValueOf[U]
  ): Arbitrary[F[T, Equal[U]]] =
    arbitraryRefType(Gen.const(wu.value))
}
