package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.boolean.Or
import org.scalacheck.{Arbitrary, Gen}

/** Module that provides `Arbitrary` instances for logical predicates. */
object boolean extends BooleanInstances

trait BooleanInstances {

  implicit def orArbitrary[F[_, _], T, A, B](implicit
      rt: RefType[F],
      arbA: Arbitrary[F[T, A]],
      arbB: Arbitrary[F[T, B]]
  ): Arbitrary[F[T, A Or B]] = {
    val genA = arbA.arbitrary.map(rt.unwrap)
    val genB = arbB.arbitrary.map(rt.unwrap)
    arbitraryRefType(Gen.oneOf(genA, genB))
  }
}
